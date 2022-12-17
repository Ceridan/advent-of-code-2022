(ns advent-of-code-2022.day17
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]))

(def figures [{:width 4 :height 1 :blocks [[0 0] [1 0] [2 0] [3 0]]}
               {:width 3 :height 3 :blocks [[1 0] [0 1] [1 1] [2 1] [1 2]]}
               {:width 3 :height 3 :blocks [[0 0] [1 0] [2 0] [2 1] [2 2]]}
               {:width 1 :height 4 :blocks [[0 0] [0 1] [0 2] [0 3]]}
               {:width 2 :height 2 :blocks [[0 0] [1 0] [0 1] [1 1]]}])

(defn- print-chamber
  ([chamber max-y round] (print-chamber chamber max-y round #{}))
  ([chamber max-y round figure-pos]
    (println)
    (println "Round: " round)
    (let [chs (->> (for [y (range max-y 0 -1) x (range 0 7)] [x y])
                   (map #(cond (contains? chamber %) \#
                               (contains? figure-pos %) \@
                               :else \.))
                   (partition 7))]
      (doseq [line chs]
        (println "|" (apply str line) "|"))
      (println "+---------+"))))

(defn- move-left
  [chamber figure x y]
  (if (= x 0)
    x
    (let [bs (->> (:blocks figure)
                     (map #(vector (+ (dec x) (first %)) (+ y (second %))))
                     (map #(contains? chamber %)))]
      (if (true? (some true? bs))
        x
        (dec x)))))

(defn- move-right
  [chamber figure x y]
  (if (> (+ x (:width figure)) 6)
    x
    (let [bs (->> (:blocks figure)
                     (map #(vector (+ (inc x) (first %)) (+ y (second %))))
                     (map #(contains? chamber %)))]
      (if (true? (some true? bs))
        x
        (inc x)))))

(defn- move-down
  [chamber figure x y]
  (let [bs (->> (:blocks figure)
         (map #(vector (+ x (first %)) (+ (dec y) (second %))))
         (map #(contains? chamber %)))]
  (if (true? (some true? bs))
    y
    (dec y))))

(defn- recalculate-chamber
  [chamber figure x y]
  (let [blocks (map #(vector (+ x (first %)) (+ y (second %))) (:blocks figure))
        max-y (apply max (map #(second %) blocks))]
    [(into chamber blocks) max-y]))

(defn- play-tetris
  [jets rounds]
  (loop [chamber (into #{} (map #(vector % 0) (range 0 7)))
         rounds-left rounds
         [x y] [2 4]
         max-y 0
         jet-idx 0
         figure-idx 0]
    (if (= rounds-left 0)
      max-y
      (let [figure (get figures figure-idx)
            jet (get jets jet-idx)
            new-x (if (= jet \<) (move-left chamber figure x y) (move-right chamber figure x y))
            new-y (move-down chamber figure new-x y)
            next-jet-idx (mod (inc jet-idx) (count jets))]
        (if (= new-y y)
          (let [[new-chamber figure-max-y] (recalculate-chamber chamber figure new-x new-y)
                max-y (max max-y figure-max-y)]
            (recur new-chamber (dec rounds-left) [2 (+ max-y 4)] max-y next-jet-idx (mod (inc figure-idx) (count figures))))
          (recur chamber rounds-left [new-x new-y] max-y next-jet-idx figure-idx))))))

(defn part1
  [data rocks-count]
  (play-tetris (char-array data) rocks-count))

(defn part2
  [data rocks-count]
  (play-tetris (char-array data) rocks-count))

(defn -main
  []
  (let [day "17"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data 2022))
    (printf "Day %s, part 2: %s\n", day, (part2 data 1000000000000))))

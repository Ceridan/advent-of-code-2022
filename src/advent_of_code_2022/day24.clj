(ns advent-of-code-2022.day24
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]])
  (:import [clojure.lang PersistentQueue]))

(def directions {:N [-1 0] :W [0 -1] :S [1 0] :E [0 1]})

(defrecord Blizzard [dir route])

(defn- parse-blizzard-map
  [data]
  (->> data
       (map-indexed #(vector %1 (map-indexed (fn [idx val] (vector idx val)) %2)))
       (map #(map (fn [chr] (vector [(first %) (first chr)] (second chr))) (second %)))
       (apply concat)
       (into {})))

(defn- get-valley-size
  [blizzard-map]
  (let [max-y (apply max (map first (keys blizzard-map)))
        max-x (apply max (map second (keys blizzard-map)))]
    [(inc max-y) (inc max-x)]))

(defn- create-blizzard
  [blizzard-map valley-size pos]
  (let [bdir (get blizzard-map pos)
        [y x] pos
        [rows cols] valley-size]
    (cond
      (= bdir \^) (->Blizzard :N (mapv #(vector % x) (vec (concat (range y 0 -1) (range (- rows 2) y -1)))))
      (= bdir \<) (->Blizzard :W (mapv #(vector y %) (vec (concat (range x 0 -1) (range (- cols 2) x -1)))))
      (= bdir \v) (->Blizzard :S (mapv #(vector % x) (vec (concat (range y (dec rows)) (range 1 y)))))
      (= bdir \>) (->Blizzard :E (mapv #(vector y %) (vec (concat (range x (dec cols)) (range 1 x))))))))

(defn- get-all-blizzards
  [blizzard-map valley-size]
  (loop [pos (first (keys blizzard-map))
         tail (rest (keys blizzard-map))
         blizzards []]
    (cond
      (nil? pos) blizzards
      (contains? #{\^ \< \v \>} (get blizzard-map pos)) (recur (first tail) (rest tail) (conj blizzards (create-blizzard blizzard-map valley-size pos)))
      :else (recur (first tail) (rest tail) blizzards))))

(defn- get-blizzards-next-pos
  [blizzards t]
  (loop [blizzard (first blizzards)
         tail (rest blizzards)
         positions #{}]
    (cond
      (nil? blizzard) positions
      :else (recur (first tail) (rest tail) (conj positions (get (:route blizzard) (mod t (count (:route blizzard)))))))))

(defn- get-all-blizzard-positions
  [blizzards rounds]
    (loop [t 0
           positions {}]
      (cond
        (= t rounds) positions
        :else (recur (inc t) (assoc positions t (get-blizzards-next-pos blizzards t))))))

(defn- add-dir
  [pos dir]
  (let [[y x] pos
        [dy dx] (get directions dir)]
    [(+ y dy) (+ x dx)]))

(defn- walk
  [blizzard-map]
  (let [map-fn (partial get blizzard-map)
        size (get-valley-size blizzard-map)
        [rows cols] size
        rounds (* (- rows 2) (- cols 2))
        blizzards (get-all-blizzards blizzard-map size)
        blizzard-positions (get-all-blizzard-positions blizzards rounds)
        start [0 1]
        finish [(dec rows) (- cols 2)]]
    (loop [queue (into PersistentQueue/EMPTY [(conj start 0)])]
      (let [[y x t] (peek queue)]
        ;(print y x t "\n")
        (cond
          (= [y x] finish) t
          :else (let [t (inc t)
                      blizz-pos (get blizzard-positions (mod t rounds))
                      new-queue (pop queue)
                      new-queue (let [npos (add-dir [y x] :S)] (if (or (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos t))))
                      new-queue (let [npos (add-dir [y x] :E)] (if (or (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos t))))
                      new-queue (let [npos (add-dir [y x] :W)] (if (or (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos t))))
                      new-queue (let [npos (add-dir [y x] :N)] (if (or (<= (first npos) 0) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos t))))
                      new-queue (if (contains? blizz-pos [y x]) new-queue (conj new-queue (conj [y x] t)))]
                  ;(print blizz-pos "\n")
                  (recur new-queue)))))))

(defn part1
  [data]
  (->> data
       (parse-blizzard-map)
       (walk)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "24"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

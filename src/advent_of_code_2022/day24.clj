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
  [blizzard-map size rounds]
  (let [blizzards (get-all-blizzards blizzard-map size)]
    (loop [t 0
           positions {}]
      (cond
        (= t rounds) positions
        :else (recur (inc t) (assoc positions t (get-blizzards-next-pos blizzards t)))))))

(defn- add-dir
  [pos dir]
  (let [[y x] pos
        [dy dx] (get directions dir)]
    [(+ y dy) (+ x dx)]))

(defn- get-size-round-and-positions
  [blizzard-map]
  (let [size (get-valley-size blizzard-map)
        [rows cols] size
        rounds (* (- rows 2) (- cols 2))
        start [0 1]
        finish [(dec rows) (- cols 2)]]
    [size rounds start finish]))

(defn- walk
  [blizzard-map blizzard-positions size rounds start finish current-time]
  (let [map-fn (partial get blizzard-map)
        [rows _] size]
    (loop [queue (into PersistentQueue/EMPTY [(conj start current-time)])
           visited {}]
      (let [[y x t] (peek queue)
            vts (get visited [y x] #{})]
        (cond
          (= [y x] finish) t
          (contains? vts (mod t rounds)) (recur (pop queue) visited)
          :else (let [new-visited (assoc visited [y x] (conj vts (mod t rounds)))
                      new-t (inc t)
                      blizz-pos (get blizzard-positions (mod new-t rounds))
                      new-queue (pop queue)
                      new-queue (let [npos (add-dir [y x] :S)] (if (or (> (first npos) (dec rows)) (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos new-t))))
                      new-queue (let [npos (add-dir [y x] :E)] (if (or (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos new-t))))
                      new-queue (let [npos (add-dir [y x] :W)] (if (or (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos new-t))))
                      new-queue (let [npos (add-dir [y x] :N)] (if (or (< (first npos) 0) (= (map-fn npos) \#) (contains? blizz-pos npos)) new-queue (conj new-queue (conj npos new-t))))
                      new-queue (if (contains? blizz-pos [y x]) new-queue (conj new-queue (conj [y x] new-t)))]
                  (recur new-queue new-visited)))))
    ))

(defn part1
  [data]
  (let [blizzard-map (parse-blizzard-map data)
        [size rounds start finish] (get-size-round-and-positions blizzard-map)
        blizzard-positions (get-all-blizzard-positions blizzard-map size rounds)]
    (walk blizzard-map blizzard-positions size rounds start finish 0)))

(defn part2
  [data]
  (let [blizzard-map (parse-blizzard-map data)
        [size rounds start finish] (get-size-round-and-positions blizzard-map)
        blizzard-positions (get-all-blizzard-positions blizzard-map size rounds)
        walk-fn (partial walk blizzard-map blizzard-positions size rounds)]
    (->> 0
         (walk-fn start finish)
         (walk-fn finish start)
         (walk-fn start finish))))

(defn -main
  []
  (let [day "24"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

(ns advent-of-code-2022.day22
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

(def directions {:E [0 1]
                 :S [1 0]
                 :W [0 -1]
                 :N [-1 0]})

(defn- parse-monkey-map
  [data]
  (->> data
       (map-indexed #(vector (inc %1) (map-indexed (fn [idx val] (vector (inc idx) val)) %2)))
       (map #(map (fn [chr] (vector [(first %) (first chr)] (second chr))) (second %)))
       (apply concat)
       (remove #(= (last %) \space))
       (into {})))

(defn- parse-path
  [data]
  (map #(if (or (= "R" %) (= "L" %)) % (Integer/parseInt %))
       (-> data
           (str/replace "R" "|R|")
           (str/replace "L" "|L|")
           (str/split #"\|"))))

(defn- parse-monkey-data
  [data]
  (let [data (remove empty? data)]
    [(parse-monkey-map (drop-last data)) (parse-path (last data))]))

(defn- rotate
  [current-dir rotation]
  (cond
    (= (:E directions) current-dir) (if (= rotation "L") (:N directions) (:S directions))
    (= (:S directions) current-dir) (if (= rotation "L") (:E directions) (:W directions))
    (= (:W directions) current-dir) (if (= rotation "L") (:S directions) (:N directions))
    (= (:N directions) current-dir) (if (= rotation "L") (:W directions) (:E directions))))

(defn- get-wrap-pos
  [monkey-map pos dir]
  (let [ks (keys monkey-map)]
    (cond
      (= (:E directions) dir) (apply min-key second (filter #(= (first %) (first pos)) ks))
      (= (:S directions) dir) (apply min-key first (filter #(= (second %) (second pos)) ks))
      (= (:W directions) dir) (apply max-key second (filter #(= (first %) (first pos)) ks))
      (= (:N directions) dir) (apply max-key first (filter #(= (second %) (second pos)) ks)))))

(defn- move
  [monkey-map pos dir val]
  (loop [pos pos
         steps val]
    (let [[y x] pos
          [dy dx] dir
          new-pos (vector (+ y dy) (+ x dx))
          new-pos (if (contains? monkey-map new-pos) new-pos (get-wrap-pos monkey-map pos dir))]
      (cond
        (= steps 0) pos
        (= (get monkey-map new-pos) \#) pos
        (= (get monkey-map new-pos) \.) (recur new-pos (dec steps))))))

(defn- follow-path
  [monkey-map monkey-path]
  (let [initial-position (->> monkey-map
                              (filter #(= (first (first %)) 1))
                              (remove #(= (last %) \#))
                              (map first)
                              (apply min-key second))
        initial-direction (:E directions)]
    (loop [path monkey-path
           pos initial-position
           dir initial-direction]
      (let [next (first path)]
        (cond
          (empty? path) [pos dir]
          (= next "L") (recur (rest path) pos (rotate dir next))
          (= next "R") (recur (rest path) pos (rotate dir next))
          :else (recur (rest path) (move monkey-map pos dir next) dir))))))

(defn part1
  [data]
  (let [[monkey-map monkey-path] (parse-monkey-data data)
        [[y x] dir] (follow-path monkey-map monkey-path)
        dir-val (cond (= (:E directions) dir) 0 (= (:S directions) dir) 1 (= (:W directions) dir) 2 (= (:N directions) dir) 3)]
    (+ (* y 1000) (* x 4) dir-val)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "22"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

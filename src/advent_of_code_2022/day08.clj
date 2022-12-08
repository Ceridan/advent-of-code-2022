(ns advent-of-code-2022.day08
  (:require [advent-of-code-2022.utils :refer [read-input-as-integer-grid]]
            [clojure.set :as set]))

(defn- get-indexed-grid
  [grid]
    (->> (for [row-idx (range (count grid))
              col-idx (range (count (get grid 0)))]
          [(get (get grid row-idx) col-idx) (list row-idx col-idx)])
        (partition (count (get grid 0)))))

(defn- process-tree-row
  [row]
  (loop [visible #{}
         row row
         prev -1]
    (if (empty? row)
      visible
      (let [[current pos] (first row)]
        (if (>= prev current)
          (recur visible (rest row) (max prev current))
          (recur (conj visible pos) (rest row) (max prev current)))))))

(defn- process-tree
  [grid]
  (let [indexed-grid (get-indexed-grid grid)
        rows (map #(process-tree-row %) indexed-grid)
        rows-rev (map #(process-tree-row %) (map reverse indexed-grid))
        transposed-grid (apply map vector indexed-grid)
        cols (map #(process-tree-row %) transposed-grid)
        cols-rev (map #(process-tree-row %) (map reverse transposed-grid))]
    (->> (flatten [rows rows-rev cols cols-rev])
         (apply set/union))))

(defn part1
  [data]
  (count (process-tree data)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "08"
        data (read-input-as-integer-grid (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))




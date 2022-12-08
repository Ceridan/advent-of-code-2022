(ns advent-of-code-2022.day08
  (:require [advent-of-code-2022.utils :refer [read-input-as-integer-grid]]))

(defn- out-of-bounds?
  [grid pos]
  (let [[row-idx col-idx] pos
        row-size (count grid)
        col-size (count (get grid 0))]
    (cond
      (< row-idx 0) true
      (>= row-idx row-size) true
      (< col-idx 0) true
      (>= col-idx col-size) true
      :else false)))

(defn- calculate-tree-cover-in-direction
  [grid pos next-pos-fn]
  (let [[row-idx col-idx] pos
        height (get (get grid row-idx) col-idx)]
    (loop [grid grid
           next-pos (next-pos-fn pos)
           distance 0]
      (if (out-of-bounds? grid next-pos)
        (list nil distance)
        (let [[next-row-idx next-col-idx] next-pos
              next-height (get (get grid next-row-idx) next-col-idx)]
          (if (>= next-height height)
            (list next-pos (inc distance))
            (recur grid (next-pos-fn next-pos) (inc distance))))))))

(defn- process-tree-grid
  [grid]
  (let [top-fn (fn [pos] (let [[row-idx col-idx] pos] (list (dec row-idx) col-idx)))
        left-fn (fn [pos] (let [[row-idx col-idx] pos] (list row-idx (dec col-idx))))
        bottom-fn (fn [pos] (let [[row-idx col-idx] pos] (list (inc row-idx) col-idx)))
        right-fn (fn [pos] (let [[row-idx col-idx] pos] (list row-idx (inc col-idx))))]
    (for [row-idx (range (count grid))
          col-idx (range (count (get grid 0)))]
      [(calculate-tree-cover-in-direction grid (list row-idx col-idx) top-fn)
       (calculate-tree-cover-in-direction grid (list row-idx col-idx) left-fn)
       (calculate-tree-cover-in-direction grid (list row-idx col-idx) bottom-fn)
       (calculate-tree-cover-in-direction grid (list row-idx col-idx) right-fn)])))

(defn part1
  [data]
  (->> data
       process-tree-grid
       (map (fn [tree] (map first tree)))
       (map #(filter nil? %))
       (remove empty?)
       count))

(defn part2
  [data]
  (->> data
       process-tree-grid
       (map (fn [tree] (map second tree)))
       (map #(reduce * %))
       (apply max)))

(defn -main
  []
  (let [day "08"
        data (read-input-as-integer-grid (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

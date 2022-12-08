(ns advent-of-code-2022.day08
  (:require [advent-of-code-2022.utils :refer [read-input-as-integer-grid]]))

(defn- calculate-row
  [idx row]
  (let [row (vec row)
        height (get row idx)
        candidates (drop (inc idx) row)
        visible (take-while #(> height %) candidates)
        visible-count (count visible)]
    (if (= (count candidates) visible-count)
      [1 visible-count]
      [0 (inc visible-count)])))

(defn- calculate-grid
  [grid]
  (let [row-aligned-grid grid
        col-aligned-grid (vec (apply map vector grid))
        last-row-idx (dec (count grid))
        last-col-idx (dec (count (get grid 0)))]
    (for [row-idx (range (count grid))
          col-idx (range (count (get grid 0)))]
      [(calculate-row (- last-row-idx row-idx) (reverse (get col-aligned-grid col-idx)))
       (calculate-row (- last-col-idx col-idx) (reverse (get row-aligned-grid row-idx)))
       (calculate-row row-idx (get col-aligned-grid col-idx))
       (calculate-row col-idx (get row-aligned-grid row-idx))])))

(defn part1
  [data]
  (->> data
       calculate-grid
       (map (fn [tree] (map first tree)))
       (map #(apply max %))
       (reduce +)))

(defn part2
  [data]
  (->> data
       calculate-grid
       (map (fn [tree] (map second tree)))
       (map #(reduce * %))
       (apply max)))

(defn -main
  []
  (let [day "08"
        data (read-input-as-integer-grid (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

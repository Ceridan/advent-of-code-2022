(ns advent-of-code-2022.dayXX
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]))

(defn part1
  [data]
  nil)

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "XX"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

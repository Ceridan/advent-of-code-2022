(ns advent-of-code-2022.day01
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]))

(defn part1
  [data]
  data)

(defn part2
  [data]
  data)

(defn -main
  [& args]
  (let [day "01"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

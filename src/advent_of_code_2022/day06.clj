(ns advent-of-code-2022.day06
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]))

(defn- search-first-message-marker
  [data size]
  (->> (char-array data)
       (partition size 1)
       (map #(into #{} %))
       (map count)
       (take-while #(< % size))
       count
       (+ size)))

(defn part1
  [data]
  (search-first-message-marker data 4))

(defn part2
  [data]
  (search-first-message-marker data 14))

(defn -main
  []
  (let [day "06"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

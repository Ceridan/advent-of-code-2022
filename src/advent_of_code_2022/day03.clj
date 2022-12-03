(ns advent-of-code-2022.day03
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.set :as set]))

(defn- get-priority
  [ch]
  (let [val (int ch)
        a (int \a)
        A (int \A)]
    (if (< val a)
      (+ (- val A) 27)
      (+ (- val a) 1))))

(defn- calculate-rucksacks
  [rucksacks]
  (->> rucksacks
       (map #(into #{} %))
       (apply set/intersection)
       (map #(get-priority %))
       (reduce +)))

(defn part1
  [data]
  (->> data
       (map #(char-array %))
       (map #(split-at (/ (count %) 2) %))
       (map #(calculate-rucksacks %))
       (reduce +)))

(defn part2
  [data]
  (->> data
       (map #(char-array %))
       (partition 3)
       (map #(calculate-rucksacks %))
       (reduce +)))

(defn -main
  []
  (let [day "03"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

(ns advent-of-code-2022.day04
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-pairs
  [data]
  (->> data
       (re-matches #"(\d+)-(\d+),(\d+)-(\d+)")
       rest
       (map #(Integer/parseInt %))
       (partition 2)))

(defn- check-pair-intersection
  [pair include]
  (let [[[l1 r1] [l2 r2]] pair]
    (cond
      (and (true? include) (<= l1 l2) (>= r1 r2)) 1
      (and (true? include) (<= l2 l1) (>= r2 r1)) 1
      (and (false? include) (<= l2 r1) (>= r2 l1)) 1
      (and (false? include) (<= l1 r2) (>= r1 l2)) 1
      :else 0)))

(defn part1
  [data]
  (->> data
       (map parse-pairs)
       (map #(check-pair-intersection % true))
       (reduce +)))

(defn part2
  [data]
  (->> data
       (map parse-pairs)
       (map #(check-pair-intersection % false))
       (reduce +)))

(defn -main
  []
  (let [day "04"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

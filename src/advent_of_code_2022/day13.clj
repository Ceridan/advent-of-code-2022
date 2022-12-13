(ns advent-of-code-2022.day13
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn- parse-packet-pairs
  [data]
  (->> data
       str/split-lines
       (remove str/blank?)
       (map str/trim)
       (map edn/read-string)
       (partition 2)
       (map-indexed #(vector (inc %1) %2))))

(defn- right-order?
  [pair]
  (loop [p1 (first pair)
         p2 (second pair)]
    (let [el1 (first p1)
          el2 (first p2)
          el1 (if (and (vector? el2) (not (vector? el1))) [el1] el1)
          el2 (if (and (vector? el1) (not (vector? el2))) [el2] el2)]
      (cond
        (empty? p1) true
        (empty? p2) false
        (= el1 el2) (recur (rest p1) (rest p2))
        (and (vector? el1) (vector? el2)) (right-order? [el1 el2])
        :else (< el1 el2)))))

(defn part1
  [data]
  (->> data
       parse-packet-pairs
       (map #(vector (first %) (right-order? (second %))))
       (filter second)
       (map first)
       (reduce +)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "13"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

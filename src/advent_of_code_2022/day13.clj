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
       (map edn/read-string)))

(defn- compare-packets
  [p1 p2]
  (loop [p1 p1
         p2 p2]
    (cond
      (= p1 p2) 0
      (and (int? p1) (int? p2)) (cond (< p1 p2) -1 (> p1 p2) 1 :else 0)
      (and (vector? p1) (int? p2)) (compare-packets p1 [p2])
      (and (vector? p2) (int? p1)) (compare-packets [p1] p2)
      (empty? p1) -1
      (empty? p2) 1
      :else (let [res (compare-packets (first p1) (first p2))]
              (if (= res 0) (recur (rest p1) (rest p2)) res)))))

(defn part1
  [data]
  (->> data
       parse-packet-pairs
       (partition 2)
       (map-indexed #(vector (inc %1) (compare-packets (first %2) (second %2))))
       (filter #(= -1 (second %)))
       (map first)
       (reduce +)))

(defn part2
  [data]
  (->> (conj (parse-packet-pairs data) [[2]] [[6]])
       (sort compare-packets)
       (map-indexed #(vector (inc %1) %2))
       (filter #(or (= (second %) [[2]]) (= (second %) [[6]])))
       (map first)
       (reduce *)))

(defn -main
  []
  (let [day "13"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

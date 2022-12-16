(ns advent-of-code-2022.day16
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

(defn- parse-valve-data
  [data]
  (->> data
       (map #(re-matches #"Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)" %))
       (map #(assoc {} :id (get % 1) :rate (Integer/parseInt (get % 2)) :tunnels (str/split (get % 3) #", ")))
       (map #(vector (:id %) %))
       (into {})))

(defn part1
  [data]
  nil)

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "16"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

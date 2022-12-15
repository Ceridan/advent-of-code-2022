(ns advent-of-code-2022.day15
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.set :as set]))

(defn- parse-sensor-data
  [data]
  (->> data
       (map #(re-matches #"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)" %))
       (map rest)
       (map (fn [row] (mapv #(Integer/parseInt %) row)))
       (map #(vector [(get % 0) (get % 1)] [(get % 2) (get % 3)]))))

(defn- get-distance
  [sensor beacon]
  (let [[sx sy] sensor
        [bx by] beacon]
    (+ (abs (- sx bx)) (abs (- sy by)))))

(defn- get-cover-distance
  [sensor beacon row-idx]
  (let [[sx sy] sensor
        beacon-dist (get-distance sensor beacon)
        row-dist (get-distance sensor [sx row-idx])
        diff (- beacon-dist row-dist)]
    (if (< diff 0)
      #{}
      (into #{} (range (- sx diff) (+ sx (inc diff)))))))

(defn- get-possible-positions
  [sensors row-idx]
  (loop [sensors sensors
         positions #{}]
    (if (empty? sensors)
      positions
      (let [[sensor beacon] (first sensors)]
        (recur (rest sensors) (into positions (get-cover-distance sensor beacon row-idx)))))))

(defn part1
  [data row-idx]
  (let [sensors (parse-sensor-data data)
        beacons (->> sensors
                     (map second)
                     (filter #(= (second %) row-idx))
                     (map first)
                     (into #{}))]
    (->> (get-possible-positions sensors row-idx)
         (remove #(contains? beacons %))
         count)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "15"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data 2000000))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))



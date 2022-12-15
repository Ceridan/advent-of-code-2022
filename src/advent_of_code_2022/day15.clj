(ns advent-of-code-2022.day15
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

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
  (let [[sx _] sensor
        beacon-dist (get-distance sensor beacon)
        row-dist (get-distance sensor [sx row-idx])
        diff (- beacon-dist row-dist)]
    (if (< diff 0)
      []
      [(- sx diff) (+ sx diff)])))

(defn- get-possible-positions
  [sensors row-idx]
  (loop [sensors sensors
         positions []]
    (if (empty? sensors)
      positions
      (let [[sensor beacon] (first sensors)
            cover-distance (get-cover-distance sensor beacon row-idx)]
        (recur (rest sensors) (if (empty? cover-distance) positions (conj positions cover-distance)))))))

(defn- get-row-beacons-count
  [sensors row-idx]
  (->> sensors
       (map second)
       (filter #(= (second %) row-idx))
       (into #{})
       count))

(defn- merge-ranges
  [ranges]
  (loop [ranges (sort ranges)
         merged []]
    (cond
      (empty? ranges) merged
      (empty? merged) (recur (rest ranges) (conj merged (first ranges)))
      :else (let [[llx lrx] (last merged)
                  [clx crx] (first ranges)]
              (if (< lrx clx)
                (recur (rest ranges) (conj merged [clx crx]))
                (recur (rest ranges) (update merged (- (count merged) 1)
                                             (fn [_] [llx (max lrx crx)]))))))))

(defn part1
  [data row-idx]
  (let [sensors (parse-sensor-data data)
        row-beacons-count (get-row-beacons-count sensors row-idx)
        ranges (merge-ranges (get-possible-positions sensors row-idx))]
    (- (->> ranges
            (map #(- (inc (second %)) (first %)))
            (reduce +))
       row-beacons-count)))

(defn part2
  [data min-y max-y]
  (let [sensors (parse-sensor-data data)]
    (loop [y min-y]
      (if (> y max-y)
        -1
        (let [ranges (merge-ranges (get-possible-positions sensors y))]
          (if (> (count ranges) 1)
            (+ (* (inc (second (first ranges))) 4000000) y)
            (recur (inc y))))))))

(defn -main
  []
  (let [day "15"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data 2000000))
    (printf "Day %s, part 2: %s\n", day, (part2 data 0 4000000))))

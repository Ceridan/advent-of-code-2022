(ns advent-of-code-2022.day10
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defn- parse-cpu-cycles
  [data]
  (map-indexed #(vector (inc %1) (Integer/parseInt %2))
               (-> data
                   (str/replace "\n" " ")
                   (str/replace "noop" "0")
                   (str/replace "addx" "0")
                   str/trim
                   (str/split #" "))))

(defn- process-cycles
  [cycles]
  (loop [cycles cycles
         X 1
         strength 0]
    (let [[cycle num] (first cycles)
          new-strength (if (= (mod cycle 40) 20) (+ strength (* X cycle)) strength)]
      (if (= cycle 220)
        new-strength
        (recur (rest cycles) (+ X num) new-strength))
      )))

(defn part1
  [data]
  (-> data
      parse-cpu-cycles
      process-cycles))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "10"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

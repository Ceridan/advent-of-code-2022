(ns advent-of-code-2022.day02
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

(defn- parse-strategy
  [data]
  (mapv #(str/split % #" ") data))

(defn part1
  [strategy]
  (->> strategy
       (map #(case %
               ["A" "X"] 4
               ["A" "Y"] 8
               ["A" "Z"] 3
               ["B" "X"] 1
               ["B" "Y"] 5
               ["B" "Z"] 9
               ["C" "X"] 7
               ["C" "Y"] 2
               ["C" "Z"] 6))
       (reduce +)))

(defn part2
  [strategy]
  (->> strategy
       (map #(case %
               ["A" "X"] 3
               ["A" "Y"] 4
               ["A" "Z"] 8
               ["B" "X"] 1
               ["B" "Y"] 5
               ["B" "Z"] 9
               ["C" "X"] 2
               ["C" "Y"] 6
               ["C" "Z"] 7))
       (reduce +)))

(defn -main
  []
  (let [day "02"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 (parse-strategy data)))
    (printf "Day %s, part 2: %s\n", day, (part2 (parse-strategy data)))))

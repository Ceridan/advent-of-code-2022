(ns advent-of-code-2022.day01
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defn- parse-calories
  [data]
  (->> (str/split data #"\n\n")
      (map str/split-lines)
      (map #(remove str/blank? %))
      (map (fn [cals] (map #(Integer/parseInt %) cals)))))

(defn part1
  [data]
  (->> data
       parse-calories
       (map #(reduce + %))
       (apply max)))

(defn part2
  [data]
  (->> data
       parse-calories
       (map #(reduce + %))
       (sort >)
       (take 3)
       (reduce +)))

(defn -main
  []
  (let [day "01"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

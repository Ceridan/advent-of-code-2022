(ns advent-of-code-2022.day01
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defn- build-calories-vector
  [data]
  (loop [calories (str/split-lines data)
         calories-by-elves []
         current 0]
    (let [calorie (first calories)]
      (cond
        (nil? calorie) (conj calories-by-elves current)
        (= calorie "") (recur (rest calories) (conj calories-by-elves current) 0)
        :else (recur (rest calories) calories-by-elves (+ current (Integer/parseInt calorie)))))))

(defn part1
  [data]
  (->> data
       build-calories-vector
       (apply max)))

(defn part2
  [data]
  (->> data
       build-calories-vector
       (sort >)
       (take 3)
       (reduce +)))

(defn -main
  []
  (let [day "01"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

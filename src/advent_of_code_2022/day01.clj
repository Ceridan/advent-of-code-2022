(ns advent-of-code-2022.day01
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]
            [clojure.data.priority-map :refer [priority-map-by]]))

(defn- build-calories-heap
  [data]
  (loop [calories (str/split-lines data)
         heap (priority-map-by >)
         current 0]
    (let [calorie (first calories)]
      (cond
        (nil? calorie) (assoc heap current current)
        (= calorie "") (recur (rest calories) (assoc heap current current) 0)
        :else (recur (rest calories) heap (+ current (Integer/parseInt calorie)))))))

(defn part1
  [data]
  (-> data
      build-calories-heap
      peek
      first))

(defn part2
  [data]
  (->> data
       build-calories-heap
       seq
       (take 3)
       (map first)
       (reduce +)))

(defn -main
  [& args]
  (let [day "01"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

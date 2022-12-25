(ns advent-of-code-2022.day25
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- dec-to-five-rev
  [decimal]
  (loop [decimal decimal
         value []]
    (if (= decimal 0)
      value
      (recur (quot decimal 5) (conj value (mod decimal 5))))))

(defn decimal-to-snafu
  [decimal]
  (let [ch-zero 48]
    (loop [rev-five (dec-to-five-rev decimal)
           modifier 0
           value []]
      (let [num (+ modifier (if (empty? rev-five) 0 (first rev-five)))]
        (cond
          (empty? rev-five) (apply str (reverse (if (= num 1) (conj value (char (+ ch-zero num))) value)))
          (= num 3) (recur (rest rev-five) 1 (conj value \=))
          (= num 4) (recur (rest rev-five) 1 (conj value \-))
          (= num 5) (recur (rest rev-five) 1 (conj value \0))
          :else (recur (rest rev-five) 0 (conj value (char (+ ch-zero num)))))))))

(defn snafu-to-decimal
  [snafu]
  (let [digits (reverse (char-array snafu))
        zero (int \0)]
    (loop [digits digits
           multiplier 1
           value 0]
      (if-let [digit (first digits)]
        (case digit
          \- (recur (rest digits) (* multiplier 5) (- value multiplier))
          \= (recur (rest digits) (* multiplier 5) (- value (* multiplier 2)))
          (recur (rest digits) (* multiplier 5) (+ value (* multiplier (- (int digit) zero)))))
        value))))

(defn part1
  [data]
  (->> data
       (map snafu-to-decimal)
       (reduce +)
       decimal-to-snafu))

(defn -main
  []
  (let [day "25"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: -\n", day)))

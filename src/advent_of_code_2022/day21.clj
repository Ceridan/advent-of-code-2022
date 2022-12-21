(ns advent-of-code-2022.day21
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-monkey-math
  [data]
  (loop [lines data
         monkey-vals {}
         monkey-ops {}]
    (let [line (first lines)]
      (if (empty? lines)
        [monkey-vals monkey-ops]
        (if-let [match (re-matches #"([a-z]{4}): (\d+)" line)]
          (recur (rest lines) (assoc monkey-vals (get match 1) (Integer/parseInt (get match 2))) monkey-ops)
          (if-let [match (re-matches #"([a-z]{4}): ([a-z]{4}) ([*+-/]{1}) ([a-z]{4})" line)]
            (recur (rest lines)
                   monkey-vals
                   (assoc monkey-ops (get match 1) {:arg1 (get match 2) :arg2 (get match 4) :op (get match 3)}))))))))

(defn- eval-expression
  [monkey-vals monkey-ops key]
  (let [monkey-eval (partial eval-expression monkey-vals monkey-ops)]
    (if-let [monkey (get monkey-ops key)]
      ((resolve (symbol (:op monkey))) (monkey-eval (:arg1 monkey)) (monkey-eval (:arg2 monkey)))
      (get monkey-vals key))))

(defn part1
  [data]
  (let [[monkey-vals monkey-ops] (parse-monkey-math data)]
    (eval-expression monkey-vals monkey-ops "root")))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "21"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

(ns advent-of-code-2022.day21
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-monkey-math
  [data]
  (loop [lines data
         monkey-ops {}
         monkey-vals {}]
    (let [line (first lines)]
      (if (empty? lines)
        [monkey-ops monkey-vals]
        (if-let [match (re-matches #"([a-z]{4}): (\d+)" line)]
          (recur (rest lines) monkey-ops (assoc monkey-vals (get match 1) (Integer/parseInt (get match 2))))
          (if-let [match (re-matches #"([a-z]{4}): ([a-z]{4}) ([*+-/]{1}) ([a-z]{4})" line)]
            (recur (rest lines)
                   (assoc monkey-ops (get match 1) {:arg1 (get match 2) :arg2 (get match 4) :op (get match 3)})
                   monkey-vals)))))))

(defn- eval-expression
  [monkey-ops monkey-vals key]
  (let [monkey-eval (partial eval-expression monkey-ops monkey-vals)]
    (if-let [monkey (get monkey-ops key)]
      ((resolve (symbol (:op monkey))) (monkey-eval (:arg1 monkey)) (monkey-eval (:arg2 monkey)))
      (get monkey-vals key))))

(defn- find-arg
  [monkey-ops key]
  (->> monkey-ops
       (filter #(or (= (get (val %) :arg1) key) (= (get (val %) :arg2) key)))
       first))

(defn- reverse-op
  [monkey-key monkey-op arg]
  (if (= (:arg1 monkey-op) arg)
    (case (:op monkey-op)
      "+" {:arg1 monkey-key :arg2 (:arg2 monkey-op) :op "-"}
      "-" {:arg1 monkey-key :arg2 (:arg2 monkey-op) :op "+"}
      "*" {:arg1 monkey-key :arg2 (:arg2 monkey-op) :op "/"}
      "/" {:arg1 monkey-key :arg2 (:arg2 monkey-op) :op "*"})
    (case (:op monkey-op)
      "+" {:arg1 monkey-key :arg2 (:arg1 monkey-op) :op "-"}
      "-" {:arg1 (:arg1 monkey-op) :arg2 monkey-key :op "-"}
      "*" {:arg1 monkey-key :arg2 (:arg1 monkey-op) :op "/"}
      "/" {:arg1 (:arg1 monkey-op) :arg2 monkey-key :op "/"})))

(defn- reverse-monkey-ops
  [monkey-ops old-root new-root]
  (loop [ops monkey-ops
         next-key new-root]
    (let [[op-key op-val] (find-arg monkey-ops next-key)
          ops (dissoc ops op-key)]
      (if (= op-key old-root)
        (let [arg (if (= (:arg1 op-val) next-key) (:arg2 op-val) (:arg1 op-val))]
          (assoc ops next-key {:arg1 old-root :arg2 arg :op "+"}))
        (recur (assoc ops next-key (reverse-op op-key op-val next-key)) op-key)))))

(defn part1
  [data]
  (let [[monkey-ops monkey-vals] (parse-monkey-math data)]
    (eval-expression monkey-ops monkey-vals "root")))

(defn part2
  [data]
  (let [[monkey-ops monkey-vals] (parse-monkey-math data)
        root-val (case (:op (get monkey-ops "root")) "+" 0 "-" 0 "*" 1 "/" 1)
        new-monkey-ops (reverse-monkey-ops monkey-ops "root" "humn")
        new-monkey-vals (assoc (dissoc monkey-vals "humn") "root" root-val)]
    (eval-expression new-monkey-ops new-monkey-vals "humn")))

(defn -main
  []
  (let [day "21"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

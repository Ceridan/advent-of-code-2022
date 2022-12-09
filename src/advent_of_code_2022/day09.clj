(ns advent-of-code-2022.day09
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-instructions
  [data]
  (->> data
       (map #(re-matches #"(U|R|D|L) (\d+)" %))
       (map rest)
       (map #(list (first %) (Integer/parseInt (second %))))))

(defn- get-new-head-pos
  [head-pos dir]
  (let [[hx hy] head-pos]
    (case
      dir "U" [hx (inc hy)]
          "R" [(inc hx) hy]
          "D" [hx (dec hy)]
          "L" [(dec hx) hy])))

(defn- get-new-tail-pos
  [new-head-pos tail-pos]
  (let [[nhx nhy] new-head-pos
        [tx ty] tail-pos]
    (if (and (<= (abs (- nhx tx)) 1) (<= (abs (- nhy ty)) 1))
      [tx ty]
      [(if (= nhx tx) tx (+ tx (/ (- nhx tx) (abs (- nhx tx)))))
       (if (= nhy ty) ty (+ ty (/ (- nhy ty) (abs (- nhy ty)))))])))

(defn- process-single-step
  [rope dir]
  (loop [new-head-pos (get-new-head-pos (first rope) dir)
         new-rope [new-head-pos]
         rope (rest rope)]
    (if (empty? rope)
      new-rope
      (let [new-tail-pos (get-new-tail-pos new-head-pos (first rope))]
        (recur new-tail-pos (conj new-rope new-tail-pos) (rest rope))))))

(defn- process-single-instruction
  [rope instruction]
  (let [[dir val] instruction]
    (loop [tail-visited #{}
           rope rope
           val val]
      (if (= val 0)
        [tail-visited rope]
        (let [new-rope (process-single-step rope dir)]
          (recur (conj tail-visited (last new-rope)) new-rope (dec val)))))))

(defn- process-instructions
  [instructions rope-size]
  (loop [instructions instructions
         tail-visited #{[0 0]}
         rope (take rope-size (repeat [0 0]))]
    (if (empty? instructions)
      (count tail-visited)
      (let [[new-tail-visited new-rope] (process-single-instruction rope (first instructions))]
        (recur (rest instructions) (into tail-visited new-tail-visited) new-rope)))))

(defn part1
  [data]
  (-> data
      parse-instructions
      (process-instructions 2)))

(defn part2
  [data]
  (-> data
      parse-instructions
      (process-instructions 10)))

(defn -main
  []
  (let [day "09"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

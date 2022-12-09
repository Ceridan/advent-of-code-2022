(ns advent-of-code-2022.day09
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-instructions
  [data]
  (->> data
       (map #(re-matches #"(U|R|D|L) (\d+)" %))
       (map rest)
       (map #(list (first %) (Integer/parseInt (second %))))))

(defn- process-step
  [tail-visited head-pos tail-pos instruction]
  (let [[dir val] instruction]
  (loop [visited tail-visited
        [hx hy]  head-pos
        [tx ty] tail-pos
        val val]
    (if (= val 0)
      [visited [hx hy] [tx ty]]
      (let [[nhx nhy] (case dir "U" [hx (inc hy)] "R" [(inc hx) hy] "D" [hx (dec hy)] "L" [(dec hx) hy])
            ntx (if (= nhx tx) tx (+ tx (/ (- nhx tx) (abs (- nhx tx)))))
            nty (if (= nhy ty) ty (+ ty (/ (- nhy ty) (abs (- nhy ty)))))]
        (cond
          (and (<= (abs (- nhx tx)) 1) (<= (abs (- nhy ty)) 1)) (recur visited [nhx nhy] [tx ty] (dec val))
          (and (= nhx tx) (> nhy ty)) [(into visited (map #(vector ntx %) (range nty (+ nty val)))) [nhx (+ nhy (dec val))] [ntx (+ nty (dec val))]]
          (and (> nhx tx) (= nhy ty)) [(into visited (map #(vector % nty) (range ntx (+ ntx val)))) [(+ nhx (dec val)) nhy] [(+ ntx (dec val)) nty]]
          (and (= nhx tx) (< nhy ty)) [(into visited (map #(vector ntx %) (range nty (- nty val) -1))) [nhx (- nhy (dec val))] [ntx (- nty (dec val))]]
          (and (< nhx tx) (= nhy ty)) [(into visited (map #(vector % nty) (range ntx (- ntx val) -1))) [(- nhx (dec val)) nhy] [(- ntx (dec val)) nty]]
          :else (recur (conj visited [ntx nty]) [nhx nhy] [ntx nty] (dec val))
          )
        ))
      )))

(defn part1
  [data]
  (loop [instructions (parse-instructions data)
         visited #{[0 0]}
         head-pos [0 0]
         tail-pos [0 0]]
    (if (empty? instructions)
      (count visited)
      (let [[visited head-pos tail-pos] (process-step visited head-pos tail-pos (first instructions))]
        (recur (rest instructions) visited head-pos tail-pos)))))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "09"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

(def instructions-example ["R 4"
                           "U 4"
                           "L 3"
                           "D 1"
                           "R 4"
                           "D 1"
                           "L 5"
                           "R 2"])



(part1 instructions-example)

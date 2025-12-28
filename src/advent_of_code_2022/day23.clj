(ns advent-of-code-2022.day23
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.set]))

(def directions {:N [-1 0] :NW [-1 -1] :W [0 -1] :SW [1 -1] :S [1 0] :SE [1 1] :E [0 1] :NE [-1 1]})
(def proposal-order [{:dir :N :cond #{:N :NE :NW}}
                     {:dir :S :cond #{:S :SE :SW}}
                     {:dir :W :cond #{:W :NW :SW}}
                     {:dir :E :cond #{:E :NE :SE}}])

(defn- parse-plant-map
  [data]
  (->> data
       (map-indexed #(vector %1 (map-indexed (fn [idx val] (vector idx val)) %2)))
       (map #(map (fn [chr] (vector [(first %) (first chr)] (second chr))) (second %)))
       (apply concat)
       (filter #(= (second %) \#))
       (into {})))

(defn- get-empty-neighbors
  [plant-map pos]
  (let [[y x] pos
        dirs (vals directions)]
    (loop [result #{}
           dir (first dirs)
           tail (rest dirs)]
      (let [[dy dx] dir]
        (cond
          (nil? dir) result
          (contains? plant-map [(+ y dy) (+ x dx)]) (recur result (first tail) (rest tail))
          :else (recur (conj result [dy dx]) (first tail) (rest tail)))))))

(defn- propose
  [plant-map pos prop-order]
  (let [[y x] pos
        neighbors (get-empty-neighbors plant-map pos)
        cond-fn (fn [idx] (set (map #(get directions %) (:cond (get prop-order idx)))))
        dir-to-pos-fn (fn [dir] (let [[dy dx] (get directions dir)] [(+ y dy) (+ x dx)]))]
    (cond
      (= (count neighbors) 8) nil
      (clojure.set/subset? (cond-fn 0) neighbors) (dir-to-pos-fn (:dir (get prop-order 0)))
      (clojure.set/subset? (cond-fn 1) neighbors) (dir-to-pos-fn (:dir (get prop-order 1)))
      (clojure.set/subset? (cond-fn 2) neighbors) (dir-to-pos-fn (:dir (get prop-order 2)))
      (clojure.set/subset? (cond-fn 3) neighbors) (dir-to-pos-fn (:dir (get prop-order 3)))
      :else nil)))

(defn- collect-proposals
  [plant-map round]
  (let [prop-order (->> proposal-order
                        (cycle)
                        (drop round)
                        (take 4)
                        (vec))]
    (loop [proposals {}
           pos (first (keys plant-map))
           tail (rest (keys plant-map))]
      (cond
        (nil? pos) proposals
        :else (if-let [prop-pos (propose plant-map pos prop-order)]
                (recur (update proposals prop-pos (fnil conj []) pos) (first tail) (rest tail))
                (recur proposals (first tail) (rest tail)))))))

(defn- play-round
  [plant-map round]
  (let [proposals (collect-proposals plant-map round)
        actionable (reduce-kv (fn [m k v] (if (= 1 (count v)) (assoc m (first v) k) m)) {} proposals)]
    (reduce-kv (fn [pm old-pos new-pos]
                 (-> pm
                     (dissoc old-pos)
                     (assoc new-pos \#))
                 ) plant-map actionable)))

(defn- get-area
  [plant-map]
  (let [min-y (apply min (map first (keys plant-map)))
        max-y (apply max (map first (keys plant-map)))
        min-x (apply min (map second (keys plant-map)))
        max-x (apply max (map second (keys plant-map)))]
    (* (- (inc max-y) min-y) (- (inc max-x) min-x))))

(defn part1
  [data rounds]
  (let [plant-map (parse-plant-map data)]
    (loop [pm plant-map
           rseq (range rounds)]
      (cond
        (empty? rseq) (- (get-area pm) (count pm))
        :else (recur (play-round pm (first rseq)) (rest rseq))))))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "23"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data 10))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

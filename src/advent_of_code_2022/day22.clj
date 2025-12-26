(ns advent-of-code-2022.day22
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]
            [clojure.set]))

(def directions {:E [0 1]
                 :S [1 0]
                 :W [0 -1]
                 :N [-1 0]})

(defn- parse-monkey-map
  [data]
  (->> data
       (map-indexed #(vector %1 (map-indexed (fn [idx val] (vector idx val)) %2)))
       (map #(map (fn [chr] (vector [(first %) (first chr)] (second chr))) (second %)))
       (apply concat)
       (remove #(= (last %) \space))
       (into {})))

(defn- parse-path
  [data]
  (map #(if (or (= "R" %) (= "L" %)) % (Integer/parseInt %))
       (-> data
           (str/replace "R" "|R|")
           (str/replace "L" "|L|")
           (str/split #"\|"))))

(defn- parse-monkey-data
  [data]
  (let [data (remove empty? data)]
    [(parse-monkey-map (drop-last data)) (parse-path (last data))]))

(defn- enrich-scheme-with-coords
  [scheme monkey-map]
  (let [size (:side-size scheme)
        positions (for [y (range 0 (inc size)) x (range 0 (inc size))] [(* y size) (* x size)])]
    (loop [pos (first positions)
           tail (rest positions)
           side 1
           stn {}]
      (cond
        (nil? pos) (assoc scheme :side-to-coord stn)
        (contains? monkey-map pos) (recur (first tail) (rest tail) (inc side) (assoc stn side pos))
        :else (recur (first tail) (rest tail) side stn)))))

(defn- rotate
  [current-dir rotation]
  (cond
    (= (:E directions) current-dir) (if (= rotation "L") (:N directions) (:S directions))
    (= (:S directions) current-dir) (if (= rotation "L") (:E directions) (:W directions))
    (= (:W directions) current-dir) (if (= rotation "L") (:S directions) (:N directions))
    (= (:N directions) current-dir) (if (= rotation "L") (:W directions) (:E directions))))

(defn- get-wrap-flat-pos
  [monkey-map pos dir]
  (let [ks (keys monkey-map)]
    (cond
      (= (:E directions) dir) [(apply min-key second (filter #(= (first %) (first pos)) ks)) dir]
      (= (:S directions) dir) [(apply min-key first (filter #(= (second %) (second pos)) ks)) dir]
      (= (:W directions) dir) [(apply max-key second (filter #(= (first %) (first pos)) ks)) dir]
      (= (:N directions) dir) [(apply max-key first (filter #(= (second %) (second pos)) ks)) dir])))

(defn- invert-direction
  [dir]
  (cond
    (= dir :N) :S
    (= dir :W) :E
    (= dir :S) :N
    (= dir :E) :W))

(defn- pos-to-side
  [scheme pos]
  (let [size (:side-size scheme)
        side-map (:side-to-coord scheme)
        [y x] pos
        ny (- y (mod y size))
        nx (- x (mod x size))]
    (loop [side 1]
      (let [[cy cx] (get side-map side)]
        (if (and (= ny cy) (= nx cx)) side (recur (inc side)))))))

(defn- change-side
  [scheme pos dir]
  (let [[y x] pos
        size (:side-size scheme)
        dsize (dec size)
        stn (:side-to-neighbors scheme)
        cur-side (pos-to-side scheme pos)
        [cy cx] (get (:side-to-coord scheme) cur-side)
        [dy dx] [(- y cy) (- x cx)]
        next-side (get (get stn cur-side) dir)
        [ny nx] (get (:side-to-coord scheme) next-side)
        next-dir (get (clojure.set/map-invert (get stn next-side)) cur-side)
        next-inverted-dir (invert-direction next-dir)
        ]
    (cond
      (= [dir next-dir] [:N :N]) [[ny (+ nx (- dsize dx))] next-inverted-dir]
      (= [dir next-dir] [:N :W]) [[(+ ny dx) nx] next-inverted-dir]
      (= [dir next-dir] [:N :S]) [[(+ ny dsize) (+ nx dx)] next-inverted-dir]
      (= [dir next-dir] [:N :E]) [[(+ ny (- dsize dx)) (+ nx dsize)] next-inverted-dir]

      (= [dir next-dir] [:W :N]) [[ny (+ nx dy)] next-inverted-dir]
      (= [dir next-dir] [:W :W]) [[(+ ny (- dsize dy)) nx] next-inverted-dir]
      (= [dir next-dir] [:W :S]) [[(+ ny dsize) (+ nx (- dsize dy))] next-inverted-dir]
      (= [dir next-dir] [:W :E]) [[(+ ny dy) (+ nx dsize)] next-inverted-dir]

      (= [dir next-dir] [:S :N]) [[ny (+ nx dx)] next-inverted-dir]
      (= [dir next-dir] [:S :W]) [[(+ ny (- dsize dx)) nx] next-inverted-dir]
      (= [dir next-dir] [:S :S]) [[(+ ny dsize) (+ nx (- dsize dx))] next-inverted-dir]
      (= [dir next-dir] [:S :E]) [[(+ ny dx) (+ nx dsize)] next-inverted-dir]

      (= [dir next-dir] [:E :N]) [[ny (+ nx (- dsize dy))] next-inverted-dir]
      (= [dir next-dir] [:E :W]) [[(+ ny dy) nx] next-inverted-dir]
      (= [dir next-dir] [:E :S]) [[(+ ny dsize) (+ nx dy)] next-inverted-dir]
      (= [dir next-dir] [:E :E]) [[(+ ny (- dsize dy)) (+ nx dsize)] next-inverted-dir])
    ))

(defn- get-wrap-cube-pos
  [scheme monkey-map pos dir]
  (let [dir-symbol (get (clojure.set/map-invert directions) dir)
        [new-pos new-dir-symbol] (change-side scheme pos dir-symbol)]
    [new-pos (get directions new-dir-symbol)]))

(defn- move
  [monkey-map wrap-fn pos dir val]
  (loop [pos pos
         dir dir
         steps val]
    (let [[y x] pos
          [dy dx] dir
          new-pos (vector (+ y dy) (+ x dx))
          [new-pos new-dir] (if (contains? monkey-map new-pos) [new-pos dir] (wrap-fn monkey-map pos dir))]
      (cond
        (= steps 0) [pos dir]
        (= (get monkey-map new-pos) \#) [pos dir]
        (= (get monkey-map new-pos) \.) (recur new-pos new-dir (dec steps))))))

(defn- follow-path
  [monkey-map monkey-path wrap-fn]
  (let [initial-position (->> monkey-map
                              (filter #(= (first (first %)) 0))
                              (remove #(= (last %) \#))
                              (map first)
                              (apply min-key second))
        initial-direction (:E directions)]
    (loop [path monkey-path
           pos initial-position
           dir initial-direction]
      (let [next (first path)]
        (cond
          (empty? path) [pos dir]
          (= next "L") (recur (rest path) pos (rotate dir next))
          (= next "R") (recur (rest path) pos (rotate dir next))
          :else (let [[new-pos new-dir] (move monkey-map wrap-fn pos dir next)]
                  (recur (rest path) new-pos new-dir)))))))

(defn part1
  [data]
  (let [[monkey-map monkey-path] (parse-monkey-data data)
        [[y x] dir] (follow-path monkey-map monkey-path get-wrap-flat-pos)
        dir-val (cond
                  (= (:E directions) dir) 0
                  (= (:S directions) dir) 1
                  (= (:W directions) dir) 2
                  (= (:N directions) dir) 3)]
    (+ (* (inc y) 1000) (* (inc x) 4) dir-val)))

(defn part2
  [data cube-scheme]
  (let [[monkey-map monkey-path] (parse-monkey-data data)
        scheme (enrich-scheme-with-coords cube-scheme monkey-map)
        [[y x] dir] (follow-path monkey-map monkey-path (partial get-wrap-cube-pos scheme))
        dir-val (cond
                  (= (:E directions) dir) 0
                  (= (:S directions) dir) 1
                  (= (:W directions) dir) 2
                  (= (:N directions) dir) 3)]
    (+ (* (inc y) 1000) (* (inc x) 4) dir-val)))

(defn -main
  []
  (let [day "22"
        data (read-input-as-string-vector (str "day" day ".txt"))
        cube-scheme {:side-size         50
                     :side-to-neighbors {1 {:N 6, :W 4, :S 3, :E 2}
                                         2 {:N 6, :W 1, :S 3, :E 5}
                                         3 {:N 1, :W 4, :S 5, :E 2}
                                         4 {:N 3, :W 1, :S 6, :E 5}
                                         5 {:N 3, :W 4, :S 6, :E 2}
                                         6 {:N 4, :W 1, :S 2, :E 5}}}]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data cube-scheme))))

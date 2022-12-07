(ns advent-of-code-2022.day07
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

(defn- get-top-level-dir
  [current-dir]
  (let [tmp-dir (->> (str/split current-dir #"/")
                     (drop-last)
                     (str/join "/"))]
    (if (str/blank? tmp-dir) "/" tmp-dir)))

(defn- process-command-log
  [data]
  (loop [dirs (sorted-map)
         log data
         current-dir "/"]
    (if (empty? log)
      dirs
      (let [line (first log)
            cd (second (re-matches #"\$ cd ([/.a-z]+)" line))
            file (second (re-matches #"(\d+) .+" line))]
        (cond
          (some? file) (let [size (Integer/parseInt file)]
                         (recur (assoc dirs current-dir (+ (get dirs current-dir 0) size)) (rest log) current-dir))
          (some? cd) (case cd
                       "/" (recur dirs (rest log) "/")
                       ".." (recur dirs (rest log) (get-top-level-dir current-dir))
                       (recur dirs (rest log) (if (= current-dir "/") (str "/" cd) (str current-dir "/" cd))))
          :else (recur dirs (rest log) current-dir))))))

(defn- update-directory-sizes
  [dirs current-dir]
  (let [size (get dirs current-dir 0)]
    (if (or (= size 0) (= current-dir "/"))
      dirs
      (loop [dirs dirs
             current-dir (get-top-level-dir current-dir)
             size size]
        (let [current-dir-size (get dirs current-dir 0)]
          (if (= current-dir "/")
            (assoc dirs "/" (+ current-dir-size size))
            (recur (assoc dirs current-dir (+ current-dir-size size)) (get-top-level-dir current-dir) size)))))))

(defn- sum-directory-sizes
  [dirs]
  (loop [keys (keys dirs)
         dirs dirs]
    (if (empty? keys)
      dirs
      (recur (rest keys) (update-directory-sizes dirs (first keys))))))

(defn part1
  [data]
  (->> data
       process-command-log
       sum-directory-sizes
       vals
       (filter #(< % 100000))
       (reduce +)))

(defn part2
  [data]
  (let [dirs (sum-directory-sizes (process-command-log data))
        unused-space (- 70000000 (get dirs "/"))
        required-space (- 30000000 unused-space)]
    (->> (vals dirs)
         (sort >)
         (take-while #(> % required-space))
         last)))

(defn -main
  []
  (let [day "07"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

(ns advent-of-code-2022.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn read-input-as-string
  [file]
  (-> (slurp (io/resource file))
      str/trim-newline))

(defn read-input-as-string-vector
  [file]
  (-> (slurp (io/resource file))
      str/split-lines))

(defn read-input-as-integer-grid
  [file]
  (->> file
       read-input-as-string-vector
       (map #(char-array %))
       (mapv (fn [row] (let [zero (int \0)]
                         (mapv #(- (int %) zero) row))))))

(defn read-input-as-long-vector
  [file]
  (->> (slurp (io/resource file))
      str/split-lines
      (mapv #(Long/parseLong %))))

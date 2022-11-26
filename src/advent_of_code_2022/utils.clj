(ns advent-of-code-2022.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(defn read-input-as-string
  [file]
  (slurp (io/resource file)))

(defn read-input-as-string-vector
  [file]
  (->
    (slurp (io/resource file))
    (s/split-lines)))

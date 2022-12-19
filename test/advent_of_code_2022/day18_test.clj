(ns advent-of-code-2022.day18-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day18 :refer [part1 part2]]))

(def cube-small-example ["1,1,1"
                         "2,1,1"])

(def cube-large-example ["2,2,2"
                         "1,2,2"
                         "3,2,2"
                         "2,1,2"
                         "2,3,2"
                         "2,2,1"
                         "2,2,3"
                         "2,2,4"
                         "2,2,6"
                         "1,2,5"
                         "3,2,5"
                         "2,1,5"
                         "2,3,5"])

(deftest part1-test
  (testing "Part 1"
    (is (= 10 (part1 cube-small-example)))
    (is (= 64 (part1 cube-large-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 58 (part2 cube-large-example)))))

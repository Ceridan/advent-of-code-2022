(ns advent-of-code-2022.day08-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day08 :refer [part1 part2]]))

(def tree-grid-example [[3 0 3 7 3]
                        [2 5 5 1 2]
                        [6 5 3 3 2]
                        [3 3 5 4 9]
                        [3 5 3 9 0]])

(deftest part1-test
  (testing "Part 1"
    (is (= 21 (part1 tree-grid-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 8 (part2 tree-grid-example)))))

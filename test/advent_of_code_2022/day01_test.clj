(ns advent-of-code-2022.day01-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day01 :refer [part1 part2]]))

(def calories "
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
")

(deftest part1-test
  (testing "Part 1"
    (is (= 24000 (part1 calories)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 45000 (part2 calories)))))

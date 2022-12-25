(ns advent-of-code-2022.day25-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day25 :refer [part1 snafu-to-decimal decimal-to-snafu]]))

(def requirements-example ["1=-0-2"
                           "12111"
                           "2=0="
                           "21"
                           "2=01"
                           "111"
                           "20012"
                           "112"
                           "1=-1="
                           "1-12"
                           "12"
                           "1="
                           "122"])

(deftest snafu-to-decimal-test
  (testing "SNAFU to Decimal"
    (is (= 1 (snafu-to-decimal "1")))
    (is (= 2 (snafu-to-decimal "2")))
    (is (= 3 (snafu-to-decimal "1=")))
    (is (= 4 (snafu-to-decimal "1-")))
    (is (= 5 (snafu-to-decimal "10")))
    (is (= 6 (snafu-to-decimal "11")))
    (is (= 7 (snafu-to-decimal "12")))
    (is (= 8 (snafu-to-decimal "2=")))
    (is (= 9 (snafu-to-decimal "2-")))
    (is (= 10 (snafu-to-decimal "20")))
    (is (= 15 (snafu-to-decimal "1=0")))
    (is (= 20 (snafu-to-decimal "1-0")))
    (is (= 2022 (snafu-to-decimal "1=11-2")))
    (is (= 12345 (snafu-to-decimal "1-0---0")))
    (is (= 314159265 (snafu-to-decimal "1121-1110-1=0")))))

(deftest decimal-to-snafu-test
  (testing "Decimal to SNAFU"
    (is (= "1" (decimal-to-snafu 1)))
    (is (= "2" (decimal-to-snafu 2)))
    (is (= "1=" (decimal-to-snafu 3)))
    (is (= "1-" (decimal-to-snafu 4)))
    (is (= "10" (decimal-to-snafu 5)))
    (is (= "11" (decimal-to-snafu 6)))
    (is (= "12" (decimal-to-snafu 7)))
    (is (= "2=" (decimal-to-snafu 8)))
    (is (= "2-" (decimal-to-snafu 9)))
    (is (= "20" (decimal-to-snafu 10)))
    (is (= "1=0" (decimal-to-snafu 15)))
    (is (= "1-0" (decimal-to-snafu 20)))
    (is (= "1=11-2" (decimal-to-snafu 2022)))
    (is (= "1-0---0" (decimal-to-snafu 12345)))
    (is (= "1121-1110-1=0" (decimal-to-snafu 314159265)))))

(deftest part1-test
  (testing "Part 1"
    (is (= "2=-1=0" (part1 requirements-example)))))

(ns ascii-radar.core-test
  (:require [clojure.test :refer [deftest is run-tests testing]]
            [ascii-radar.core :as core]
            [clojure.string :as str]))

(def invader-1
  "--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---")

(def invader-2
  "
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o")

(deftest test-score 
  (testing "testing that score gives 1.0 when both inputs are equal"
    (is (= 1.0
           (core/score (apply str (str/split-lines (str/trim invader-1)))
                       (apply str (str/split-lines (str/trim invader-1))))))
    (is (= 1.0
           (core/score (apply str (str/split-lines (str/trim invader-2)))
                       (apply str (str/split-lines (str/trim invader-2)))))))
  
  (testing "changing just one location at the end reduces the score appropriately"
    (is (= (float 0.9886364)
           (core/score (apply str (str/split-lines (str/trim invader-1)))
            (apply str (str/split-lines (str/trim "--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo--o")))))))
  
  (testing "changing just one location at the beginning reduces the score appropriately"
    (is (= (float 0.9886364)
           (core/score (apply str (str/split-lines (str/trim invader-1)))
            (apply str (str/split-lines (str/trim "--o-----o--
o--o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---"))))))))


(deftest test-sub-sample
  (testing "basic sub-sample"
    (is (= "----o--oo-
--o-o-----
--o-------
-------o--
------o---
-o--o-----
o---------
--o-------
----------
----------"
           (str/join "\n" (core/sub-sample (str/split-lines core/radar) 10 10 0 0)))))
  
  (testing "basic sub-sample, should be not equal"
    (is (not= "----o--oo-
  --o-o-----
  --o-------
  -------o--
  ------o---
  -o--o-----
  o---------
  --o-------
  ----------
  ----------"
           (str/join "\n" (core/sub-sample (str/split-lines core/radar) 11 10 10 10))))))



(deftest test-scan-for-invader
  (testing "simplest"
    (is (= [{:location [-1 -1], :score 0.0, :invader "o", :sample "U"}
            {:location [0 -1], :score 0.0, :invader "o", :sample "U"}
            {:location [-1 0], :score 0.0, :invader "o", :sample "U"}
            {:location [0 0], :score 1.0, :invader "o", :sample "o"}]
           
           (core/scan-for-invader "o" "o" 0.0))))
  
  (testing "invader bigger than radar"
    (is (= [{:location [-1 -2], :score 0.0, :invader "o\no", :sample "U\nU"}
            {:location [0 -2], :score 0.0, :invader "o\no", :sample "U\nU"}
            {:location [-1 -1], :score 0.0, :invader "o\no", :sample "U\nU"}
            {:location [0 -1], :score 0.5, :invader "o\no", :sample "U\no"}
            {:location [-1 0], :score 0.0, :invader "o\no", :sample "U\nU"}
            {:location [0 0], :score 0.5, :invader "o\no", :sample "o\nU"}]
  
           (core/scan-for-invader "o" "o\no" 0.0)))))

(run-tests)
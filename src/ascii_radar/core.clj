(ns ascii-radar.core
  (:require [ascii-radar.sample-data :as sample-data]
            [clojure.string :as str]) 
  (:gen-class))

(def detection-threshold 0.74)

(defn score [a b]
  (let [paired (map vector a b)
        s (reduce (fn [score [ax bx]]
                    (if (= ax bx) ; we have a match
                      (+ score 1) 
                      score))
                  0 paired)]
    (float (/ s (count a)))))

(defn add-unknowns [radar w h]
  (let [middle (map (fn [l]
                      (let [u (str/join (repeat w "?"))]
                        (str u l u)))
                    radar)
        final-w (+ (count (first radar)) (* 2 w))
        start-end (repeat h (str/join (repeat final-w "?")))]
    (vec (concat start-end middle start-end))))

(defn sub-sample [sample w h x y] 
  (let [lines (subvec sample y (+ y h))]
    (map (fn [l] (subs l x (+ x w))) lines)))

(defn all-locations [sample invader-w invader-h]
  (let [sample-w (+ invader-w (count (first sample)))
        sample-h (+ invader-h (count sample))]
    (for [y (range sample-h)
          x (range sample-w)]
      [x y])))

(defn scan-for-invader [radar invader threshold]
  (let [invader-w (count (first invader))
        invader-h (count invader)
        radar' (add-unknowns radar invader-w invader-h)]
    (->> (all-locations radar invader-w invader-h)
         (reduce (fn [v [x y]]
                   (let [samp (sub-sample radar' invader-w invader-h x y)
                         s (score (str/join samp) (str/join invader))]
                     (if (>= s threshold)
                       (conj v {:location [(- x invader-w) 
                                           (- y invader-h)] 
                                :score s
                                :invader (str/join "\n" invader)
                                :sample (str/join "\n" samp)})
                       v)))
                 []))))

(defn print-results [results]
  (doseq [{:keys [location score invader sample]} results]
    (println "\n\nInvader found at: " location " detection score: " score)
    (println invader)
    (println "")
    (println sample)))

(defn gen-radar [w h]
  (repeat h (str/join (repeat w "-"))))

(defn add-invader-to-radar [radar invader x y]
  (map-indexed (fn [idx l]
                 (let [radar-w (count (first radar))
                       invader-w (count (first invader))
                       invader-h (count invader)
                       y' (- idx y)]
                   (if (and (>= y' 0) (< y' invader-h))
                     (let [invader-l (nth invader y')]
                       (-> (str (when (pos? x)
                                  (subs l 0 x))
                                (subs invader-l (if (neg? x) (abs x) 0))
                                (when (< (+ x invader-w) radar-w)
                                  (subs l (+ x invader-w))))
                           (subs 0 radar-w)))
                     l)))
               radar))

(defn -main [& args]
  (doseq [invader [sample-data/invader-1 sample-data/invader-2]]
    (print-results (scan-for-invader sample-data/radar invader detection-threshold))))
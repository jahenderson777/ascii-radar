(ns ascii-radar.core
  (:require [ascii-radar.sample-data :as sample-data]
            [clojure.string :as str]) 
  (:gen-class))

(def detection-threshold 0.74)

(defn score 
  "Calculates the similarity score between two sequences `a` and `b`.
  
  Both sequences should be of the same length. The function returns a
  floating-point score between 0.0 and 1.0 inclusive, representing the
  proportion of elements that are equal at the same positions in both sequences.
  
  A score of 1.0 indicates a 100% match, while 0.0 means no elements matched.
   
  If `a` contains '?' characters, these represent an unknown with 50% probably of a hit.

  Parameters:
  - a: A sequence of elements.
  - b: A sequence of elements of the same length as `a`.

  Returns:
  - A float between 0.0 and 1.0 representing the similarity score."
  [a b]
  (let [paired (map vector a b)
        s (reduce (fn [score [ax bx]]
                    (if (= ax bx) ; we have a match
                      (+ score 1)
                      (if (= ax \?)
                        (+ score 0.5)
                        score)))
                  0 paired)]
    (float (if (> (count a) 0)
             (/ s (count a))
             1.0))))

(defn add-unknowns
  "Adds borders around an ASCII radar sample by inserting '?' characters.
  
  The function takes a radar, which is a sequence of strings of equal length, 
  and adds horizontal and vertical borders. The borders are created by 
  repeating the '?' character. The width (w) and height (h) of the borders are 
  specified by the user.

  Parameters:
  - radar (seq of strings): The original radar, where each string represents a row of the radar.
  - w (int): The width of the horizontal border (number of '?' characters to add to each side).
  - h (int): The height of the vertical border (number of '?' rows to add to the top and bottom).

  Returns:
  - (vec of strings): A new radar with the added borders."
  [radar w h]
  (let [middle (map (fn [l]
                      (let [u (str/join (repeat w "?"))]
                        (str u l u)))
                    radar)
        final-w (+ (count (first radar)) (* 2 w))
        start-end (repeat h (str/join (repeat final-w "?")))]
    (vec (concat start-end middle start-end))))

(defn sub-sample 
  "Extracts a sub-sample from an ASCII radar.

  This function takes an ASCII radar (a sequence of strings of equal length) and extracts
  a sub-region of the radar, defined by a width (w), height (h), and starting coordinates
  (x, y) for the top-left corner of the sub-sample. The function returns the sub-sample as 
  a sequence of strings.

  Parameters:
  - radar (seq of strings): The original radar, where each string represents a row.
  - w (int): The width of the sub-sample (number of characters in each row).
  - h (int): The height of the sub-sample (number of rows to include).
  - x (int): The horizontal offset for the starting point of the sub-sample.
  - y (int): The vertical offset for the starting point of the sub-sample.

  Returns:
  - (seq of strings): A sequence representing the sub-sample of the radar."
  [radar w h x y] 
  (let [lines (subvec radar y (+ y h))]
    (map (fn [l] (subs l x (+ x w))) lines)))

(defn all-locations
  "Generates all possible [x y] coordinate locations for a radar, 
  including extra space to account for invaders that may extend off the top or left edges.

  This function calculates the total area for a radar by adding the width and height 
  of an invader to the radar's dimensions. It then returns a sequence of all [x y] 
  coordinate pairs within this extended area. The coordinates represent potential 
  positions for the invader on the radar, including positions where the invader might 
  be partially off the top or left edges.

  Parameters:
  - radar (seq of strings): The ASCII radar, a sequence of strings of equal length
  - invader-w (int): The width of the invader (in characters).
  - invader-h (int): The height of the invader (in rows).

  Returns:
  - (seq of [x y]): A sequence of [x y] coordinate pairs representing all possible 
  positions on the radar that the invader could occupy, including positions extending 
  off the top or left edges."
  [radar invader-w invader-h]
  (let [sample-w (+ invader-w (count (first radar)))
        sample-h (+ invader-h (count radar))]
    (for [y (range sample-h)
          x (range sample-w)]
      [x y])))

(defn scan-for-invader
  "Scans an ASCII radar for occurrences of a given invader pattern, returning all detected 
   invaders with details about their location and match score.

  This function scans over an ASCII radar (a sequence of strings representing a grid), searching 
   for instances of an invader pattern (also a sequence of strings). The search is performed over 
   all possible locations, and invaders are considered detected if their match score with the 
   radar region is above a specified threshold. The match score is a value between 0.0 and 1.0, 
   where higher scores indicate better matches.

  Parameters:
  - radar (seq of strings): The radar data, a sequence of strings of equal length, where each 
   string represents a row.
  - invader (seq of strings): The invader pattern, a sequence of strings to look for within 
   the radar. The invader's width and height determine the size of the search window.
  - threshold (float): The minimum match score required to consider a potential invader 
   detection. The score is a float between 0.0 and 1.0, where higher values represent more 
   confident matches.

  Returns:
  - (seq of maps): A sequence of maps, each representing a detected invader. Each map contains:
    - :location [x y]: The location of the detected invader on the radar, adjusted to give the 
       top-left corner of the invader's position.
    - :score (float): The match score between the invader and the corresponding radar sub-region.
    - :invader (string): The invader pattern that was being searched for, joined into a single string.
    - :sample (string): The radar sub-region that matched the invader, joined into a single string.
  
  Important:
  - The location returned in the map is the top-left corner of the invader pattern, indicating 
    where the invader was detected in the radar."
  [radar invader threshold]
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
(defproject max-puzzle "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.495"]
                 [reagent "0.6.0"]
                 [re-frame "0.9.2"]
                 [garden "1.3.2"]
                 [ns-tracker "0.3.1"]]
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :plugins [[lein-cljsbuild "1.1.6-SNAPSHOT"]
            [lein-shell "0.5.0"]
            [lein-pdo "0.1.1"]
            [lein-ancient "0.6.10"]
            [lein-garden "0.3.0"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/css"
                                    "target"
                                    "docs/css"
                                    "docs/img"
                                    "docs/js"
                                    "docs/favicon.ico"
                                    "docs/index.html"]
  :figwheel {:css-dirs ["resources/public/css"]
             :server-port 3450
             :repl false}
  :garden {:builds [{:id "dev"
                     :source-paths ["src/clj"]
                     :stylesheet max-puzzle.css/screen
                     :compiler {:output-to "resources/public/css/screen.css"
                                :pretty-print? true}}
                    {:id "min"
                     :source-paths ["src/clj"]
                     :stylesheet max-puzzle.css/screen
                     :compiler {:output-to "resources/public/css/screen.css"
                                :pretty-print? false}}]}
  :profiles {:dev {:plugins [[lein-figwheel "0.5.9"]]
                   :dependencies [[binaryage/dirac "1.2.0"]
                                  [binaryage/devtools "0.9.2"]
                                  [re-frisk "0.3.2"]]
                   :repl-options {:port 8230
                                  :nrepl-middleware [dirac.nrepl/middleware]
                                  :init (do
                                          (require 'dirac.agent)
                                          (dirac.agent/boot!))}}}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel {:on-jsload "max-puzzle.core/mount-root"}
                        :compiler {:main max-puzzle.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true
                                   :preloads [devtools.preload dirac.runtime.preload]}}
                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler {:main max-puzzle.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]}
  :aliases {"dist" ["do"
                    "clean,"
                    "garden" "once,"
                    "cljsbuild" "once" "min,"
                    "shell" "./release.sh"]
            "reload" ["pdo"
                      "garden" "auto,"
                      "figwheel" "dev,"]})

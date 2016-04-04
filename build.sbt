name := "ESCrudRahul"

version := "1.0"

scalaVersion := "2.11.5"


libraryDependencies  ++= {
                            Seq(
                                  "org.elasticsearch" % "elasticsearch" % "1.5.2",
                              "org.scalatest" %% "scalatest" % "2.2.6" % "test"
                            )
}

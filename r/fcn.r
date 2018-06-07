# CA & Cluster Analysis

anlys <- function(d = data, 
                  t = threshold){
  c <- dcast(d, STYLE ~ ATTR, 
             value.var = "STYLE",
             fun.aggregate = length)
  rownames(c) <- c[,1]
  c$STYLE <- NULL
  
  # convert all summed values to 0/1 abundance
  c[c != 0] <- 1
  
  # threshold >2 
  c <- quantAAR::itremove(c,t)
  
  # write out the abundance table
  write.csv(c, "data/processed/crosstab.csv")
  
  # Hierarchical clustering
  # =======================
    # > Determine the optimal number of clusters
  # > fviz_nbclust(scale(a), kmeans, method = "gap_stat")
  # see:
  # - [ ] http://www.sthda.com/english/wiki/print.php?id=239
  # - [ ] https://kkulma.github.io/2017-04-24-determining-optimal-number-of-clusters-in-your-data/?utm_content=bufferf25df&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
  
  # Determine the optimal number of clusters
  # ----------------------------------------
  c.scaled <- scale(c)
  
  # fviz_nbclust(c.scaled, kmeans, method = "wss")
  # fviz_nbclust(c.scaled, hcut, method = "wss")
  
  nbclust <- fviz_nbclust(c.scaled, hcut, method = "silhouette", 
                          hc_method = "complete")
  nbclust
  
  # extract optimal number of clusters
  # ----------------------------------
  opt.clust = as.matrix(nbclust[["data"]][["y"]])
  k <- row(opt.clust)[opt.clust==max(opt.clust)]
  
  # Clustering
  # ----------
  res <- hcut(c, k = k, stand = TRUE)
  clust.plt <- fviz_dend(res, rect = TRUE, cex = 0.5)

  
  # Correspondance Analysis
  # =======================
  res.ca <- CA(c, graph = FALSE)
  ca.plt.12 <- fviz_ca_biplot(res.ca,
                           repel = TRUE)
  
  # 2nd and 3rd axes
  ca.plt.23 <- fviz_ca_biplot(res.ca,
                               axes = c(2, 3),
                               repel = TRUE)
  # 3nd and 2rd axes
  ca.plt.32 <- fviz_ca_biplot(res.ca,
                              axes = c(3, 2),
                              repel = TRUE)
  
  # export coordinates
  # ------------------

  e <- get_ca(res.ca)
  e_xyz <- e[1]$coord[,1:3]
  
  chron <- read.csv("https://raw.githubusercontent.com/dirkseidensticker/nwCongo/master/bib/StilGrChrono.csv", 
                    encoding = "UTF-8")
  chron$STYLE <- as.character(chron$Typ)
  
  CA_export <- merge(x = e_xyz, 
                     y = chron[, c("STYLE", "FROM", "TO", "REL")], 
                     by.x = "row.names", 
                     by.y = "STYLE")
  
  colnames(CA_export) <- c("name", "x", "y", "z", "von", "bis", "fixed")
  
  CA_export$fixed[CA_export$fixed == 0 | is.na(CA_export$fixed)] <- "schwebend"
  CA_export$fixed[CA_export$fixed == 1] <- 'fix'
  
  write.table(CA_export, 
              "data/processed/CA_export.tsv", 
              quote=FALSE, 
              sep='\t', 
              row.names = FALSE)
  
  # Build plot
  # ==========
  cluster_col <- plot_grid(clust.plt, 
                          nbclust, 
                          ncol = 1,
                          labels = c("C", "D"))
  
  plt1 <- plot_grid(ca.plt.12,
                    ca.plt.23,
                    cluster_col,
                    ncol = 3, 
                    labels = c("A", "B", ""), 
                    rel_heights = c(1, 1, 1))

  # CA Plot
  plt2 <- plot_grid(ca.plt.12,
                    ca.plt.32,
                    ncol = 2, 
                    labels = c("A", "B"), 
                    rel_heights = c(1, 1))
  
  # Individual ggplots of CA output
  # ===============================
  
  # ggplot of styles with age as fill
  # ---------------------------------
  
  
  
  # ggplot of attributes with type as shape & variante as fill
  # ----------------------------------------------------------
  
  
  
  # Build returned list
  # ===================
  
  pltList <- list("basicPlot" = plt1, 
                  "caPlot" = plt2)
  
  return(pltList)
  
  # # combiing clusters & ca:
  # c <- read.csv("data/processed/crosstab.csv", row.names = 1)
  # c.ca <- CA(c, 
  #            graph = FALSE)
  # c.hcpc <- HCPC(c.ca, 
  #                graph = FALSE, 
  #                max = 3)
  
  # # Dendrogram
  # clust.plt <- fviz_dend(c.hcpc)
  # Individuals facor map
  # ca.plt <- fviz_cluster(c.hcpc)
  # bottom_row <- plot_grid(clust.plt, 
  #                         NULL, 
  #                         align = 'h', 
  #                         labels = c("B", "C"))
  # plt <- plot_grid(ca.plt, 
  #                  bottom_row,
  #                  ncol = 1, 
  #                  labels = c("A", ""), 
  #                  rel_heights = c(2, 1))
  # ggsave("img/Anlys2.png", width = 10, height = 10)
}
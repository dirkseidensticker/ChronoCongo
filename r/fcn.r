# CA & Cluster Analysis

anlys <- function(d = data,
                  t = threshold, 
                  n = name){

  # setup filepath for output
  p <- "output/"

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
  write.csv(c, paste(p, n, "_crosstab.csv", sep = ""))
  
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
  
  nbclust <- fviz_nbclust(c.scaled, 
                          hcut, 
                          method = "silhouette", 
                          hc_method = "complete")
  nbclust
  
  # extract optimal number of clusters
  # ----------------------------------
  opt.clust = as.matrix(nbclust[["data"]][["y"]])
  k <- row(opt.clust)[opt.clust==max(opt.clust)]
  
  # Clustering
  # ----------
  res <- hcut(c, 
              k = k, 
              stand = TRUE)
  clust.plt <- fviz_dend(res, 
                         rect = TRUE, 
                         cex = 0.5, 
                         palette = "jco", rect_border = "jco", rect_fill = TRUE)

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
              paste(p, n, "_CAexport.tsv", sep = ""),
              quote=FALSE, 
              sep='\t', 
              row.names = FALSE)
  
  # Build basic CA plot
  # ===================
  plt1 <- plot_grid(ca.plt.12,
                    ca.plt.32,
                    ncol = 2, 
                    labels = c("A", "B"), 
                    rel_heights = c(1, 1))
  
  ggsave(paste(p, n, "_ca.png", sep = ""), 
         plot = plt1, width = 20, height = 10)
  
  # Combining Clusters & CA
  # =======================

  c.ca <- CA(c, 
             graph = FALSE)
  # c.hcpc <- HCPC(c.ca, graph = FALSE, max = 3)
  
  c.hcpc <- hcut(c, 
                 k = k, 
                 stand = TRUE)
  
  # clust.plt <- fviz_dend(c.hcpc) # Dendrogram
  # Individuals facor map
  ca.plt12 <- fviz_cluster(c.hcpc,
                           palette = "jco", 
                           show.clust.cent = FALSE,
                           ggtheme = theme_minimal())
  
  ca.plt32 <- fviz_cluster(c.hcpc, 
                           axes = c(3, 2), 
                           palette = "jco", 
                           show.clust.cent = FALSE,
                           ggtheme = theme_minimal())
  
  # combine plots
  cluster_col <- plot_grid(clust.plt, 
                           nbclust, 
                           ncol = 1,
                           labels = c("C", "D"))
  
  plt2 <- plot_grid(ca.plt12,
                    ca.plt32,
                    cluster_col,
                    ncol = 3, 
                    labels = c("A", "B", ""), 
                    rel_heights = c(1, 1, 1))
  
  ggsave(paste(p, n, "_cluster.png", sep = ""), 
         plot = plt2, width = 20, height = 10)
  
  # Individual ggplots of CA output
  # ===============================
  
  style <- get_ca_row(res.ca)
  style_xyz <- as.data.frame(style[1]$coord[,1:3])
  
  attr <- get_ca_col(res.ca)
  attr_xyz <- as.data.frame(attr[1]$coord[,1:3])
  
  # ggplot of styles with age as fill
  # ---------------------------------
  chron$MeanAge <- (chron$FROM + chron$TO) / 2
  
  style_xyz <- merge(x = style_xyz, 
                     y = chron[, c("STYLE", "MeanAge")], 
                     by.x = "row.names", 
                     by.y = "STYLE")
  
  plt3 <- ggplot(style_xyz, aes(x = `Dim 1`, y = `Dim 2`, label = Row.names)) + 
    geom_text_repel() + 
    geom_point(aes(fill = MeanAge), 
               shape = 21, 
               size = 3) + 
    scale_fill_viridis() + 
    coord_equal() +
    geom_hline(yintercept = 0, linetype="dashed") +
    geom_vline(xintercept = 0, linetype="dashed") + 
    theme_light() + 
    theme(legend.position="bottom")
  
  # ggplot of attributes with type as shape & variante as fill
  # ----------------------------------------------------------
  attr_xyz$Attribute <- gsub("\\..*", "", row.names(attr_xyz))
  attr_xyz$Variety <- gsub("^.*?\\.", "", row.names(attr_xyz))
  
  plt4 <- ggplot(attr_xyz, aes(x = `Dim 1`, 
                               y = `Dim 2`, 
                               label = rownames(attr_xyz), 
                               shape = attr_xyz$Attribute, 
                               fill = rownames(attr_xyz))) +
    geom_text_repel() + 
    geom_point(size = 3) +
    coord_equal() +
    scale_shape_manual(name = "Feature", 
                       values = c(21, 22, 23, 24)) + 
    scale_fill_discrete(guide = FALSE) + 
    geom_hline(yintercept = 0, linetype="dashed") +
    geom_vline(xintercept = 0, linetype="dashed") + 
    theme_light() + 
    theme(legend.position="bottom")
  
  plt5 <- plot_grid(plt3,
                    plt4,
                    ncol = 2, 
                    labels = c("A", "B"), 
                    rel_heights = c(1, 1))
  
  ggsave(paste(p, n, "_ggplot.png", sep = ""), 
         plot = plt5, width = 20, height = 10)
  
  # Build returned list
  # ===================
  
  pltList <- list("basicPlot" = plt1, 
                  "clustPlot" = plt2, 
                  "ggplot" = plt5, 
                  "hclust" = res)
  
  return(pltList)
  
}
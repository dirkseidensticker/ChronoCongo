# see:
# - [ ] http://www.sthda.com/english/rpkgs/factoextra/
# - [ ] http://www.sthda.com/english/articles/31-principal-component-methods-in-r-practical-guide/113-ca-correspondence-analysis-in-r-essentials/#export-plots-to-pdfpng-files

library("FactoMineR")
library("factoextra")
library("reshape2")

# load data
d <- read.csv("data/base/StyleAttributes.csv", encoding = 'UTF-8')

# build abundace table
a <- dcast(d, STYLE ~ ATTR, 
           fun.aggregate = length)
rownames(a) <- a[,1]
a$STYLE <- NULL

# Correspondence Analysis
res.ca <- CA(a, graph = FALSE)

fviz_ca_biplot(res.ca, repel = TRUE)
ggsave("img/CA.png", width = 12, height = 12)

# export coordinates
e <- get_ca(res.ca)

write.table(e[1]$coord[,1:2], 
            "data/processed/CA_export.tsv", 
            quote=FALSE, 
            sep='\t', 
            col.names = NA)

# Compute hierarchical clustering and cut into 4 clusters

# Determine the optimal number of clusters
# fviz_nbclust(scale(a), kmeans, method = "gap_stat")

res <- hcut(a, k = 4, stand = TRUE)
fviz_dend(res, rect = TRUE, cex = 0.5,
          k_colors = c("#00AFBB","#2E9FDF", "#E7B800", "#FC4E07"))
ggsave("img/Clust.png", width = 12, height = 6)


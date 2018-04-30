# see:
# - [ ] http://www.sthda.com/english/rpkgs/factoextra/
# - [ ] http://www.sthda.com/english/articles/31-principal-component-methods-in-r-practical-guide/113-ca-correspondence-analysis-in-r-essentials/#export-plots-to-pdfpng-files

library("data.table")
library("dplyr")
library("FactoMineR")
library("factoextra")
library("plyr")
library("reshape2")

#library(devtools)
#install_github('ISAAKiel/quantAAR')
library(quantAAR)

# load data
d <- read.csv("data/base/StyleAttributes.csv", encoding = 'UTF-8')

# replace vessel shape typology with universal typology for diachronic question:
k <- read.csv("data/base/Wotzka1995_GefFormenKonkordanz.csv")
k$TypOrig <- paste("GefTyp", k$TypHPW, sep = "")
k$TypNew <- paste("GefTyp", k$TypDS, sep = "")

d$ATTR <- mapvalues(d$ATTR, 
          from = k$TypOrig, 
          to = k$TypNew)

# filter out rims as there is no universal typolgy jet
d <- filter(d, !grepl("Rand", ATTR))

# filter out fabrics as no thorough analysis has been performed jet
#d <- filter(d, !grepl("Fabric", ATTR))

# build abundace table
c <- dcast(d, STYLE ~ ATTR, 
           fun.aggregate = length)
rownames(c) <- c[,1]
c$STYLE <- NULL

# convert all summed values to 0/1 abundance
c[c != 0] <- 1

# threshold >2 
c <- quantAAR::itremove(c,2)

# write out the abundance table
write.csv(c, "data/processed/crosstab.csv")

# Correspondence Analysis
res.ca <- CA(c, graph = FALSE)

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

res <- hcut(c, k = 6, stand = TRUE)
fviz_dend(res, rect = TRUE, cex = 0.5)
#k_colors = c("#00AFBB","#2E9FDF", "#E7B800", "#FC4E07"))
ggsave("img/Clust.png", width = 12, height = 6)


d <- read.csv("data/Congo_StyleAttributes.csv", encoding = 'UTF-8')

k <- read.csv("data/Wotzka1995_GefFormenKonkordanz.csv")
k$TypOrig <- paste("GefTyp.", k$TypHPW, sep = "")
k$TypNew <- paste("GefTyp.", k$TypDS, sep = "")

d$ATTR <- mapvalues(d$ATTR, 
                    from = k$TypOrig, 
                    to = k$TypNew)

d <- filter(d, !grepl("Rand", ATTR))

c <- dcast(d, STYLE ~ ATTR, 
           value.var = "STYLE",
           fun.aggregate = length)
rownames(c) <- c[,1]
c$STYLE <- NULL

c[c != 0] <- 1

res <- hcut(c, 
            k = 4, 
            stand = TRUE)
fviz_dend(res, 
          rect = TRUE, 
          cex = 0.5, 
          palette = "jco", 
          rect_border = "jco", 
          rect_fill = TRUE)

fviz_cluster(res,
             palette = "jco", 
             show.clust.cent = FALSE,
             ggtheme = theme_minimal())
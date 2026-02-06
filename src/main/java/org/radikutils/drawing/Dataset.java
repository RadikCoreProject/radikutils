package org.radikutils.drawing;

import org.jfree.data.category.CategoryDataset;
import org.radikutils.parser.Parser;

@FunctionalInterface
public interface Dataset<T extends Parser> {
    CategoryDataset run(T parser);
}

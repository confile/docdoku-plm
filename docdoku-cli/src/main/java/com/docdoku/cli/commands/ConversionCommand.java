/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2014 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.docdoku.cli.commands;

import com.docdoku.cli.tools.ScriptingTools;
import com.docdoku.core.product.Conversion;
import com.docdoku.core.product.PartIterationKey;
import com.docdoku.core.services.IProductManagerWS;
import org.kohsuke.args4j.Option;

/**
 *
 * @author Morgan Guimard
 */
public class ConversionCommand extends AbstractCommandLine {

    @Option(name="-w", aliases = "--workspace", required = true, metaVar = "<workspace>", usage="workspace on which operations occur")
    protected String workspace;

    @Option(metaVar = "<partnumber>", required = true, name = "-o", aliases = "--part", usage = "the part number of the part to verify the existence of conversion")
    private String number;

    @Option(metaVar = "<revision>", required = true, name="-r", aliases = "--revision", usage="specify revision of the part to analyze ('A', 'B'...)")
    private String revision;

    @Option(name="-i", aliases = "--iteration", metaVar = "<iteration>", usage="specify iteration of the part to retrieve ('1','2', '24'...); default is the latest")    private int iteration;

    private IProductManagerWS productS;

    @Override
    public void execImpl() throws Exception {
        productS = ScriptingTools.createProductService(getServerURL(), user, password);
        PartIterationKey pK = new PartIterationKey(workspace,number,revision, iteration);
        Conversion conversion = productS.getConversion(pK);
        output.printConversion(conversion);
    }

    @Override
    public String getDescription() {
        return "Retrieve conversion status for given part iteration";
    }
}

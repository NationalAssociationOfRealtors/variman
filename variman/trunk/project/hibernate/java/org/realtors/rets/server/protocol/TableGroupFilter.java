package org.realtors.rets.server.protocol;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import org.apache.log4j.Logger;

import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.Group;

/**
 * Represents a set of table INCLUDE or EXCLUDE filters attached to
 * groups.
 */
public class TableGroupFilter
{
    /**
     * Initializes a new instance of the TableGroupFilter class
     * with the default settings.
     */
    public TableGroupFilter()
    {
        mAllTables = new LinkedHashMap<String, Set<MTable>>();
        this.mIncludeRules = new LinkedHashMap<String, Set<MTable>>();
        this.mExcludeRules = new LinkedHashMap<String, Set<MTable>>();
    }

    /**
     * Retrieves the filtered field set for the specified group,
     * resource, and rets class arguments.  If there is not filtered set
     * available, the full table is returned.
     *
     * @param group     the group
     * @param resource  the rets resource
     * @param retsClass the rets class
     * @return a set of the fields available to the group.
     */
    public Set<MTable> findTables(Group group, String resource,
                                      String retsClass)
    {
        Set<Group> groups = new LinkedHashSet<Group>();
        groups.add(group);

        return this.findTables(groups, resource, retsClass);
    }

    /**
     * Retrieves the filtered field set for a specified resource/class,
     * given several groups.
     *
     * @param groups    A Set of Group
     * @param resource  the RETS resource of interest
     * @param retsClass the RETS class of interest
     * @return a set of fields that are visible.
     * @see Group
    */
    public Set<MTable> findTables(Set<Group> groups, String resource,
                                      String retsClass)
    {
        if (resource == null || resource.length() == 0) {
            throw new IllegalArgumentException("resource must be non-null and non-empty");
        }

        if (retsClass == null || retsClass.length() == 0) {
            throw new IllegalArgumentException("retsClass must be non-null and non-empty");
        }

        String tableKey = resource + ":" + retsClass;
        
        if (groups == null || groups.isEmpty())
        {
            return mAllTables.get(tableKey);
        }

        Set<MTable> allIncludeTables = new LinkedHashSet<MTable>();
        Set<MTable> allExcludeTables = new LinkedHashSet<MTable>();
        Set<MTable> filteredTables = new LinkedHashSet<MTable>(this.mAllTables.get(tableKey));

        for (Group aGroup : groups) {
            String ruleKey = aGroup.getName() + ":" + tableKey;

            if (this.mIncludeRules.containsKey(ruleKey)) {
                allIncludeTables.addAll(this.mIncludeRules.get(ruleKey));
            }

            if (this.mExcludeRules.containsKey(ruleKey)) {
                allExcludeTables.addAll(this.mExcludeRules.get(ruleKey));
            }
        }

        // With all the include and exclude rules collected.  Determine the
        // final filteredTables set
        if (!allIncludeTables.isEmpty()) {
            // If there are include rules, intersect
            filteredTables.retainAll(allIncludeTables);
        }

        // remove all fields which were explicitly excluded
        filteredTables.removeAll(allExcludeTables);

        return filteredTables;
    }

    /**
     * Adds the specified {@link GroupRules} to this TableGroupFilter.
     * 
     * @param rules the {@link GroupRules} to be added
     * 
     * TODO: The Administration application needs to be updated to only allow one entry per resource/class combo.  Otherwise it breaks this method.
     */
    public void addRules(GroupRules rules)
    {
        List filterRules = rules.getFilterRules();
        String groupName = rules.getGroupName();

        for (int i = 0; i < filterRules.size(); i++)
        {
            FilterRule filterRule = (FilterRule) filterRules.get(i);
            String tableKey = buildKey(filterRule.getResource(),
                                       filterRule.getRetsClass());
            String rulesKey = buildKey(filterRule.getResource(), 
                                       filterRule.getRetsClass(), 
                                       groupName);
            Set<MTable> tables = mAllTables.get(tableKey);

            if (tables == null)
            {
                LOG.warn("No tables found for " + tableKey +
                         ".  Skipping rule " + filterRule);
                continue;
            }

            Set<MTable> filteredTables = new LinkedHashSet<MTable>();
            for (MTable aTable : tables) {
                if (filterRule.containsSystemName(aTable.getSystemName())) {
                    filteredTables.add(aTable);
                }
            }

            if (filterRule.getType() == FilterRule.INCLUDE) {
                this.mIncludeRules.put(rulesKey, filteredTables);
            } else if (filterRule.getType() == FilterRule.EXCLUDE) {
                this.mExcludeRules.put(rulesKey, filteredTables);
            }
        }
    }

    /**
     * Sets the list of tables for the specified resource and class name.
     *
     * @param resource      The name of the resource
     * @param retsClass     The name of the RETS class
     * @param tables        The {@link Set<E>} of tables for the resource/class.
     */
    public void setTables(String resource, String retsClass,
                          Set<MTable> tables)
    {
        mAllTables.put(TableGroupFilter.buildKey(resource, retsClass), tables);
    }

    /**
     * Build a standard hash key for a resource and class name.
     *
     * @param resource      The resource name
     * @param retsClass     The class name
     * @return A key for resource and class to be used with hashes.
     */
    protected static String buildKey(String resource, String retsClass) {
        if (resource == null || resource.length() == 0) {
            throw new IllegalArgumentException("resource name must be non-null and non-empty");
        }

        if (retsClass == null || retsClass.length() == 0) {
            throw new IllegalArgumentException("retsClass name must be non-null and non-empty");
        }

        return resource + ":" + retsClass;
    }

    /**
     * Build a standard hash key for a specific group, resource, and class name
     *
     * @param group         The {@link Group}
     * @param resource      The name of the resource
     * @param retsClass     The name of the RETS class.
     * @return A key for the group/resource/class to be used with hashes.
     */
    protected static String buildKey(Group group, String resource, String retsClass) {
        if (group == null) {
            throw new IllegalArgumentException("group must be non-null");
        }

        return buildKey(resource, retsClass, group.getName());
    }

    /**
     * Build a standard hash key for the specific group, resource, and class names.
     *
     * @param resource      The resource name
     * @param retsClass     The RETS class name
     * @param groupName     The name of the group
     * @return A key for the group/resource/class to be used with hashes.
     */
    protected static String buildKey(String resource, String retsClass, String groupName) {
        if (groupName == null || groupName.length() == 0) {
            throw new IllegalArgumentException("groupName must be non-null and non-empty");
        }

        return groupName + ":" + buildKey(resource, retsClass);
    }

    private static final Logger LOG =
        Logger.getLogger(TableGroupFilter.class);

    /** 
     * Contains all tables for a given resource and class. 
     * 
     * The key value is of the form {resource}:{retsClass}
     */
    private Map<String, Set<MTable>> mAllTables;

    // key values are of the form {groupName}:{resource}:{retsClass}
    private Map<String, Set<MTable>> mIncludeRules;
    private Map<String, Set<MTable>> mExcludeRules;
}

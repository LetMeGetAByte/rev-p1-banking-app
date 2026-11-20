public record AccountsResult(Map<Integer, List<Account>> result) {
  // impl to get specific results i.e
  public List<Account> get(Integer hash) { return result().get(hash); }


  
}
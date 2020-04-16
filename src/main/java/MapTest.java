import java.util.*;
 
public class MapTest {
 
	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("2020-03-14", 2);
		map.put("2020-02-14", 1);
		map.put("2020-01-14", 4);
		map.put("2020-01-15", 3);
		map.put("2020-02-29", 3);

		System.out.println(map);

		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
 
		// 对HashMap中的key 进行排序
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
//				System.out.println(o1.getKey()+"   ===  "+o2.getKey());
				return (o1.getKey()).toString().compareTo(o2.getKey().toString());
			}
		});



		// 对HashMap中的key 进行排序后  显示排序结果
		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
			System.out.print(id + "  ");
		}
		
		System.out.println();
		
		// 对HashMap中的 value 进行排序
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o1.getValue()).toString().compareTo(o2.getValue().toString());
			}
		});
 
		// 对HashMap中的 value 进行排序后  显示排序结果
		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
			System.out.print(id + "  ");
		}
 
	}
 
}
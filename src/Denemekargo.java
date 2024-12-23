// Geliştirilmiş Kargo Takip Sistemi

import java.util.*;
import java.util.TreeSet;

class Gonderi {
    String gonderiID;
    String tarih;
    String teslimDurumu;
    int teslimSuresi;
    int sehirID;
    String sehirAdi;
    String ilceAdi;
    

    public Gonderi(String gonderiID, String tarih,int sehirID,String sehirAdi,String ilceAdi, String teslimDurumu, int teslimSuresi) {
        this.gonderiID = gonderiID;
        this.tarih = tarih;
        this.teslimDurumu = teslimDurumu;
        this.teslimSuresi = teslimSuresi;
        this.sehirID=sehirID;
        this.sehirAdi=sehirAdi;
        this.ilceAdi=ilceAdi;
        
    }

    @Override
    public String toString() {
        return "Gonderi ID: " + gonderiID + ", Tarih: " + tarih +" , Teslimat Rotasi: "+ sehirAdi + " " + ilceAdi + ", Teslim Durumu: " + teslimDurumu + ", Teslim Suresi: " + teslimSuresi + " gun";
    }
}

class Musteri {
    String musteriID;
    String isim;
    LinkedList <Gonderi> gonderiGecmisi;
    Stack <Gonderi> sonGonderiler;

    public Musteri(String musteriID, String isim) {
        this.musteriID = musteriID;
        this.isim = isim;
        this.gonderiGecmisi = new LinkedList<>();
        this.sonGonderiler = new Stack<>();
    }

    public void gonderiEkle(Gonderi gonderi) {
        gonderiGecmisi.add(gonderi);
        if (sonGonderiler.size() == 5) {
            sonGonderiler.remove(0); // İlk gönderiyi çıkararak stack'i güncel tutar
        }
        sonGonderiler.push(gonderi);
    }

    public void gonderiGecmisiGoster() {
        if (gonderiGecmisi.isEmpty()) {
            System.out.println("Gonderi gecmisi bos.");
        } else {
            for (Gonderi gonderi : gonderiGecmisi) {
                System.out.println(gonderi);
            }
        }
    }

    public void sonGonderileriGoster() {
        if (sonGonderiler.isEmpty()) {
            System.out.println("Son gonderi bulunmamaktadir.");
        } else {
            System.out.println("Son Gonderiler:");
            for (int i = sonGonderiler.size() - 1; i >= 0; i--) {
                System.out.println(sonGonderiler.get(i));
            }
        }
    }

    @Override
    public String toString() {
        return "Musteri ID: " + musteriID + ", Isim: " + isim ;
    }
}
class KargoSorgulama {

    static List<Musteri> musteriler = new ArrayList<>(); // Müşteri listesi


    // Binary Search
    public static Gonderi binarySearch(List<Gonderi> sortedList, String targetId) {
        long startTime = System.nanoTime();
        int low = 0, high = sortedList.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Gonderi midGonderi = sortedList.get(mid);

            if (midGonderi.gonderiID.equals(targetId)) {
                return midGonderi;
            } else if (midGonderi.gonderiID.compareTo(targetId) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
            long endTime = System.nanoTime();
            long durationInNano = endTime - startTime; 
            double durationInSeconds = durationInNano / 1_000_000_000.0;
            System.out.println("Calisma suresi: " + durationInSeconds + " seconds");
        }
        return null; // Kargo bulunamadı
    }

    // Merge Sort
    public static void mergeSort(List<Gonderi> kargolar, int left, int right) {
        long startTime=System.nanoTime();
        if (left < right) {
            int mid = (left + right) / 2;

            // Sol ve sağ parçayı sıralıyoruz
            mergeSort(kargolar, left, mid);
            mergeSort(kargolar, mid + 1, right);

            // Parçaları birleştiriyoruz
            merge(kargolar, left, mid, right);
            long endTime = System.nanoTime();
            long durationInNano = endTime - startTime; 
            double durationInSeconds = durationInNano / 1_000_000_000.0;
            System.out.println("Calisma suresi: " + durationInSeconds + " seconds");
        }
    }

    private static void merge(List<Gonderi> kargolar, int left, int mid, int right) {
        List<Gonderi> temp = new ArrayList<>();
        int i = left, j = mid + 1;

        while (i <= mid && j <= right) {
            if (kargolar.get(i).teslimSuresi <= kargolar.get(j).teslimSuresi) {
                temp.add(kargolar.get(i++));
            } else {
                temp.add(kargolar.get(j++));
            }
        }

        while (i <= mid) {
            temp.add(kargolar.get(i++));
        }

        while (j <= right) {
            temp.add(kargolar.get(j++));
        }

        for (int k = 0; k < temp.size(); k++) {
            kargolar.set(left + k, temp.get(k));
        }
    }
}
class RotaSystem {
    // TreeMap -> Şehir ID'sine göre sıralı tutar, her şehir bir HashMap ile ilçelerini tutar
    private TreeMap<Integer, Map<String, List<String>>> deliveryMap;
    private String root;
    private final int TIME_PER_LEVEL = 5; // Her seviye için teslimat süresi (örneğin, 5 dakika)


    public RotaSystem(String root) {
        deliveryMap = new TreeMap<>();
        this.root=root;
    }

    // Yeni kargo ekleme
    public void addDelivery(int cityId, String cityName, String districtName) {
        // Şehir ID'sine göre kontrol et
        deliveryMap.putIfAbsent(cityId, new HashMap<>());
        Map<String, List<String>> city = deliveryMap.get(cityId);

        // Şehir ismi kontrolü ve ilçeyi ekleme
        city.putIfAbsent(cityName, new ArrayList<>());
        if (!city.get(cityName).contains(districtName)) {
            city.get(cityName).add(districtName);
        }
    }
    // Şehir ID'sine göre derinlik hesaplama
private int calculateDepth(int cityId) {
    // Şehir ID'sine kadar olan şehir sayısını bul
    return deliveryMap.headMap(cityId).size() + 1; // Derinlik 1’den başlıyor
}

    // Teslimat süresini hesapla ve en kısa rotayı yazdır
public void calculateShortestDeliveryTime() {
    System.out.println("Ana Sube: " + root);
    int shortestTime = Integer.MAX_VALUE; // En kısa süreyi tutar
    String shortestPath = ""; // En kısa süreli rotayı tutar

    // Şehir ID'sine göre gruplayarak ilçeleri toplamak için bir Map oluşturuyoruz
    Map<Integer, Map<String, List<String>>> groupedCities = new TreeMap<>();

    // Verileri şehir ID'ye göre grupla
    for (Map.Entry<Integer, Map<String, List<String>>> entry : deliveryMap.entrySet()) {
        int cityId = entry.getKey();
        Map<String, List<String>> cityInfo = entry.getValue();

        // Şehirleri grupla ve ilçeleri ekle
        for (Map.Entry<String, List<String>> cityEntry : cityInfo.entrySet()) {
            String cityName = cityEntry.getKey();
            List<String> districts = cityEntry.getValue();

            // Eğer aynı şehir ID'si varsa, ilçeleri ekleyelim
            groupedCities.putIfAbsent(cityId, new HashMap<>());
            Map<String, List<String>> cityGroup = groupedCities.get(cityId);
            cityGroup.putIfAbsent(cityName, new ArrayList<>());
            cityGroup.get(cityName).addAll(districts); // Aynı şehir altında ilçeleri topla
        }
    }

    // Şehir ID'sine göre sıralı ve ilçeleri gruplayarak çıktı al
    for (Map.Entry<Integer, Map<String, List<String>>> entry : groupedCities.entrySet()) {
        int cityId = entry.getKey();
        Map<String, List<String>> cityInfo = entry.getValue();

        for (Map.Entry<String, List<String>> cityEntry : cityInfo.entrySet()) {
            String cityName = cityEntry.getKey();
            List<String> districts = cityEntry.getValue();

            System.out.print("Rota: " + cityId + " - " + cityName); // Şehir ID ve adı
            System.out.print("\n    Ilceler: ");
            for (int i = 0; i < districts.size(); i++) {
                System.out.print(districts.get(i));
                if (i < districts.size() - 1) {
                    System.out.print(", "); // İlçeleri virgülle ayır
                }
            }
            System.out.println(); // Yeni satır
        }
    }

    // En kısa teslimat süresini hesapla (aynı şekilde)
    for (Map.Entry<Integer, Map<String, List<String>>> entry : deliveryMap.entrySet()) {
        int cityId = entry.getKey();
        Map<String, List<String>> cityInfo = entry.getValue();

        for (Map.Entry<String, List<String>> cityEntry : cityInfo.entrySet()) {
            String cityName = cityEntry.getKey();
            List<String> districts = cityEntry.getValue();

            for (String district : districts) {
                // Derinlik hesapla ve teslimat süresini bul
                int depth = calculateDepth(cityId);
                int deliveryTime = depth * 1; // Örneğin her seviye 7 saat

                System.out.println("Rota: " + cityName + " - " + district + " | Sure: " + deliveryTime + " gun");

                // En kısa teslimat süresini güncelle
                if (deliveryTime < shortestTime) {
                    shortestTime = deliveryTime;
                    shortestPath = cityName + " - " + district;
                }
            }
        }
    }

    // En kısa rotayı yazdır
    System.out.println("\nEn Kisa Teslimat Rotasi: " + shortestPath);
    System.out.println("Teslimat Suresi: " + shortestTime + " gun");
}
}


 public class Denemekargo {
    static LinkedList <Musteri> musteriler = new LinkedList<>();
    static PriorityQueue <Gonderi> kargoOncelikSirasi = new PriorityQueue<>(Comparator.comparingInt(g -> g.teslimSuresi));
    static RotaSystem rota=new RotaSystem("Medeniyet Kargo Merkezi (Istanbul)");
    public Denemekargo() {
    }
    static Scanner scanner = new Scanner(System.in);

    public static void yeniMusteriEkle() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();
        System.out.print("Isim: ");
        String isim = scanner.nextLine();

        Musteri musteri = new Musteri(musteriID, isim);
        musteriler.add(musteri);
        System.out.println("Musteri basariyla eklendi!");
    }

    public static void musteriGonderiEkle() {
        
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();
        

        for (Musteri musteri : musteriler) {
            if (musteri.musteriID.equals(musteriID)) {
                System.out.print("Gonderi ID: ");
                String gonderiID = scanner.nextLine();
                System.out.print("Tarih: ");
                String tarih = scanner.nextLine();
                System.out.println("Plaka kodu :");
                int sehirID=scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                System.out.println("Teslimat Rotasi: ");
                System.out.println("Sehir Ismi: ");
                String sehirAdi=scanner.nextLine();
                System.out.println("Ilce Ismi: ");
                String ilceAdi=scanner.nextLine();
                System.out.print("Teslim Durumu (Teslim Edildi/Teslim Edilmedi): ");
                String teslimDurumu = scanner.nextLine();
                System.out.print("Teslim Suresi (gun): ");
                int teslimSuresi = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                 
                long startTime=System.nanoTime();
                Gonderi gonderi = new Gonderi(gonderiID, tarih,sehirID, sehirAdi,ilceAdi, teslimDurumu, teslimSuresi);
                rota.addDelivery(sehirID, sehirAdi, ilceAdi);
                musteri.gonderiEkle(gonderi);
                kargoOncelikSirasi.add(gonderi);
                long endTime = System.nanoTime();
                double duration = (endTime - startTime)/ 1_000_000_000.0; 
       
                System.out.println("Calisma suresi: " + duration +" second" );
                System.out.println("Gonderi basariyla eklendi!");
                return;
            }
        }

        System.out.println("Musteri bulunamadi!");
    }

    public static void oncelikliKargoIsle() {
        long startTime = System.nanoTime();
        if (kargoOncelikSirasi.isEmpty()) {
            System.out.println("Islenecek kargo bulunmamaktadir.");
        } else {
            Gonderi oncelikliGonderi = kargoOncelikSirasi.poll();
            long endTime = System.nanoTime();
            System.out.println("Oncelikli kargo isleniyor: " + oncelikliGonderi);
            double duration = (endTime - startTime)/ 1_000_000_000.0;
        
            // Süreyi yazdır
            System.out.println("Calisma suresi: " + duration +" second " );
        }
    }

    public static void musteriGonderiGoster() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();

        for (Musteri musteri : musteriler) {
            if (musteri.musteriID.equals(musteriID)) {
                System.out.println("Musteri Bilgileri: " + musteri);
                musteri.gonderiGecmisiGoster();
                return;
            }
        }

        System.out.println("Musteri bulunamadi!");
    }

    
    public static void musteriSonGonderileriGoster() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();

        for (Musteri musteri : musteriler) {
            if (musteri.musteriID.equals(musteriID)) {
                musteri.sonGonderileriGoster();
                return;
            }
        }

        System.out.println("Musteri bulunamadi!");
    }

   public static void kargoDurumuSorgula(String kargoId) {
        // Teslim Edilmiş ve Edilmemiş Gönderiler
        List<Gonderi> teslimEdilmis = new ArrayList<>();
        List<Gonderi> teslimEdilmemis = new ArrayList<>();

        // Müşteri ve gönderi geçmişini kontrol et
        for (Musteri musteri : musteriler) {
            for (Gonderi gonderi : musteri.gonderiGecmisi) {
                if (gonderi.teslimDurumu.equalsIgnoreCase("Teslim Edildi")) {
                    teslimEdilmis.add(gonderi);
                } else {
                    teslimEdilmemis.add(gonderi);
                }
            }
        }

        // Teslim Edilmiş Kargoları ID'ye göre sıralama
        teslimEdilmis.sort(Comparator.comparing(g -> g.gonderiID));

        // Teslim Edilmemiş Kargoları teslim süresine göre Merge Sort ile sıralama
        
        KargoSorgulama.mergeSort(teslimEdilmemis, 0, teslimEdilmemis.size() - 1);
        
        

        // Teslim Edilmiş Kargoları Binary Search ile Arama
        Gonderi teslimEdilmisSonuc = KargoSorgulama.binarySearch(teslimEdilmis, kargoId);
        

        if (teslimEdilmisSonuc != null) {
            System.out.println("Kargo ID: " + kargoId + " - Teslim Edildi.");
            System.out.println("Teslim Tarihi: " + teslimEdilmisSonuc.tarih);
            return;
        }

        // Teslim Edilmemiş Kargoları Arama
        for (Gonderi gonderi : teslimEdilmemis) {
            if (gonderi.gonderiID.equals(kargoId)) {
                System.out.println("Kargo ID: " + kargoId + " - Teslim Edilmedi.");
                System.out.println("Tahmini Teslim Tarihi:En gec 5 is gunu icerisindedir.");
                return;
            }
        }

        // Kargo bulunamadı
        System.out.println("Kargo ID: " + kargoId + " ile ilgili gönderi bulunamadı.");
    }

    public static void menu() {
        while (true) {
            System.out.println("\n--- Kargo Takip Sistemi ---");
            System.out.println("1. Yeni Musteri Ekle");
            System.out.println("2. Musteriye Gonderi Ekle");
            System.out.println("3. Musteri Gonderi Gecmisini Goster");
            System.out.println("4. Oncelikli Kargo Isle");
            System.out.println("5. Son Gonderileri Goster");
            System.out.println("6. Eklenen Rotalari Goster");
            System.out.println("7. Kargolari Sorgula");
            System.out.println("8. Cikis");
            System.out.print("Seciminiz: ");

            try {
                int secim = Integer.parseInt(scanner.nextLine());
                switch (secim) {
                    case 1:
                        yeniMusteriEkle();
                        break;
                    case 2:
                        musteriGonderiEkle();
                        break;
                    case 3:
                        musteriGonderiGoster();
                        break;
                    case 4:
                        oncelikliKargoIsle();
                        break;
                    case 5:
                        musteriSonGonderileriGoster();
                        break;
                    case 6:
                        rota.calculateShortestDeliveryTime();
                        break;
                    case 7:
                        System.out.print("Kargo ID: ");
                        String gonderiID = scanner.nextLine();
                        kargoDurumuSorgula(gonderiID);
                        break;
                    case 8:
                        System.out.println("Sistemden cikiliyor...");
                        return;
                    default:
                        System.out.println("Gecersiz secim. Lutfen 1 ile 8 arasinda bir sayi girin.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Hata: Lutfen gecerli bir sayi girin.");
            }
        }
    }

    
    public static void main(String[] args) {
        menu();
        
    }
}

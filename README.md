Ana Amaç
Müşteri ve gönderi bilgilerinin yönetimi, kargo rotalarının hesaplanması, kargo sıralaması ve teslimat süreçlerinin izlenmesi için bir sistem geliştirilmiştir.

Projenin Temel Özellikleri
Müşteri Yönetimi:

Müşteriler bir Musteri sınıfı ile temsil edilmiştir.
Her müşteri, gönderi geçmişi (LinkedList) ve son 5 gönderi kaydı (Stack) ile izlenir.
Yeni müşteriler eklenebilir, gönderi geçmişi ve son gönderiler sorgulanabilir.
Gönderi Takibi:

Gönderiler, Gonderi sınıfı ile temsil edilmiştir.
Gönderi bilgileri, gönderi ID'si, tarih, teslim süresi, şehir ve ilçe bilgileri gibi alanları içerir.
Gönderiler müşteri geçmişine eklenir ve teslim durumu izlenir.
Kargo Yönetimi:

Gönderiler teslim durumuna göre ayrılır:
Teslim edilmiş kargolar için Binary Search kullanılarak arama yapılır.
Teslim edilmemiş kargolar, teslim sürelerine göre Merge Sort algoritmasıyla sıralanır.
Rota Sistemi:

Şehir ID'sine ve ilçelerine göre teslimat rotası belirlenir.
Teslimat süresi, şehirlere olan uzaklığa bağlı olarak hesaplanır.
En kısa teslimat rotası ve süre hesaplanarak görüntülenir.
Öncelikli Gönderi İşleme:

Teslim süresine göre gönderiler bir PriorityQueue ile önceliklendirilir.
En kısa sürede teslim edilmesi gereken gönderi seçilip işlenir.
Performans Ölçümleri:

Algoritmaların çalışma süreleri (ör. Merge Sort, Binary Search, gönderi ekleme) nanosecond cinsinden ölçülmüş ve çıktı olarak verilmiştir.
Uygulama İşlevselliği
Bir menü sistemi ile aşağıdaki işlemler gerçekleştirilir:

Yeni müşteri ekleme.
Mevcut müşteriye yeni gönderi ekleme.
Müşteri gönderi geçmişini görüntüleme.
Öncelikli kargo işleme.
Müşteri son gönderilerini görüntüleme.
Teslimat rotalarının hesaplanması ve gösterimi.
Gönderi durumu sorgulama (Teslim Edildi/Teslim Edilmedi).
Sistemden çıkış.
Kullanılan Veri Yapıları ve Algoritmalar
TreeMap: Şehir ve ilçe bilgilerini sıralı şekilde tutmak için kullanılmıştır.
LinkedList: Müşteri gönderi geçmişinin depolanmasında kullanılmıştır.
Stack: Müşterinin son gönderilerinin izlenmesinde kullanılmıştır.
PriorityQueue: Teslim süresine göre kargoların önceliklendirilmesinde kullanılmıştır.
Binary Search: Teslim edilmiş gönderilerde hızlı arama yapmak için uygulanmıştır.
Merge Sort: Teslim edilmemiş gönderileri sıralamak için kullanılmıştır.
Proje Çıktıları
Teslimat süresi ve rota hesaplamaları başarılı bir şekilde yapılmıştır.
Gönderilerin teslim durumu sorgulanabilir.
En kısa teslimat rotası ve süre bulunabilir.
Performans ölçümleri ve kullanıcı dostu menü sistemi geliştirilmiştir.

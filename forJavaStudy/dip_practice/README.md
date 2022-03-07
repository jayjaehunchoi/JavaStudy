# 의존성 역전 원칙

의존성 역전 원칙에 대해 들어본적 있으신가요?

흔히들 객체지향의 핵심 원칙으로 **SOLID**를 알고 계실텐데요, 의존성 역전 원칙은 **DIP**로 객체지향 핵심 원칙의 다섯번째 원칙입니다.
오늘 포스팅에서는 **의존성 역전 원칙**의 개념에 대해 학습하고 직접 코드를 작성하며 **왜 이 원칙을 지켜야 하는가**에 대해 몸소 느껴보고자 합니다.

## DIP
**DIP**는 고수준 모듈을 참조하여 예상치 못한 의존성의 변화를 줄이고자 생겨난 원칙입니다.

여기서 **고수준 모듈**과 **저수준 모듈**에 대한 궁금증이 들텐데요. 과연 이 둘은 무슨 의미일까요?

> **고수준 모듈**   
> 상위 수준의 정책으로 하나의 큰 기능이라고 볼 수 있습니다. 간단하게 자바의 Interface를 생각하면 됩니다.  

> **저수준 모듈**
> 고수준 모듈의 기능을 구현하기 위해 필요한 상세 기능. 즉, 실제 구현체를 의미합니다.

예시를 통해 조금 더 명확하게 보겠습니다. 

```"파일을 받아 실제 파일은 로컬 저장소에 저장하고, 정보는 메모리 데이터 베이스에 저장한다"``` 라는 요구사항이 존재한다고 가정해봅시다.

이 때 고수준 모듈은, **이미지를 저장한다** 와 **정보를 저장한다** 정도가 되겠습니다.
그리고 저수준 모듈은 **이미지를 로컬 저장소에 저장한다**와 **정보를 메모리에 저장한다**로 설명할 수 있습니다.

그렇다면 만약 현재 구현하고 있는 코드에서 **저수준 모듈**에 직접 의존하면 어떻게 될까요?

### 저수준 모듈 직접 의존

```java
public class FileService {

    private final FakeLocalUploader fakeLocalUploader; // 저수준 모듈로 
    private final MemoryFileRepository memoryFileRepository;

    public FileService(FakeLocalUploader fakeLocalUploader, MemoryFileRepository memoryFileRepository) {
        this.fakeLocalUploader = fakeLocalUploader;
        this.memoryFileRepository = memoryFileRepository;
    }

    public void saveFile(String file) {
        // ...
        fakeLocalUploader.store(file);
        memoryFileRepository.save(file);
        // ...
    }
}
```
위 코드를 보면 문제가 전혀 없을 것 같습니다. 실제로 동작도 잘 되죠. 하지만 문제를 발생시켜볼까요?

저수준 모듈을 **S3에 파일 저장**, **Jdbc 템플릿을 사용하여 MySQL에 저장** 으로 변경하겠습니다.

단지 저수준 모듈의 요구사항이 변한 것임에도 이를 의존하고 있는 상위 코드까지 변경해야 하는 이슈가 발생합니다.
이 문제는 잦은 코드 변경을 유발하고, **버그, 사이드 이펙트** 발생 가능성을 높입니다.

그렇다면 **고수준 모듈**을 참조하면 어떨까요?

### 고수준 모듈 의존

구현 요구사항은 아래와 같습니다.
1. 상품 번호를 이용해서 DB에서 정보를 가져온다.
2. 다른 팀의 Rec ver 1 API를 통해 추천 상품 5개를 가져온다.
3. 추천 상품이 5개 미만이면 같은 카테고리의 인기상품을 가져와 5개를 채운다.

```java
public class ProductDataService {

    private static final int TOP_NUMBERS = 5;

    // 같은 패키지의 고수준 모듈 의존
    private final ProductDataRepository productDataRepository;
    private final ProductRecommender productRecommender;
    private final ProductErpService productErpService;

    public ProductDataService(ProductDataRepository productDataRepository,
                              ProductRecommender productRecommender,
                              ProductErpService productErpService) {
        this.productDataRepository = productDataRepository;
        this.productRecommender = productRecommender;
        this.productErpService = productErpService;
    }

    public List<Product> getRecommendedProductsData(final Long id) {
        Product findProduct = productDataRepository.findById(id);
        List<Product> recommendedProducts = productRecommender.getRecommendedProducts(findProduct);

        int currentProductsSize = recommendedProducts.size();
        if (currentProductsSize < TOP_NUMBERS) {
            recommendedProducts.addAll(cutProductsBySize(findProduct.getCategory(),
                    TOP_NUMBERS - currentProductsSize));
        }
        return recommendedProducts;
    }

    private List<Product> cutProductsBySize(Category category, int size) {
        List<Product> topProductsInCategory = productErpService.getTopProductsInCategory(category);
        return new ArrayList<>(topProductsInCategory.subList(0, size));
    }
}
```

코드는 **저수준 모듈**과 유사합니다. 하지만 세부 구현 요구사항이 변경되어도, 고수준 모듈을 의존하고 있기 떄문에 새로운 구현체를 만들어 조립만 하면 됩니다.
즉, 코드의 변경이 적어지고 안정성이 높아진다는 의미입니다.

게다가 의존성의 방향을 단방향으로 유지시켜주는데 큰 도움이 됩니다. 
의존성이 순환 참조하게 되면 결국 코드의 유지보수가 굉장히 힘들어지는데, 이 문제를 해결하는 key가 되는 것이죠.

## 정리

위 코드를 통해 **DIP**에 대해 어느정도 알게되었다고 생각합니다. 

하지만 DIP를 잘 설계하는 것은 어렵습니다. 직접 저수준 모듈에 직접 주입해보고, 의존성 순환 이슈도 맛보면서 경험하는 것이 답이라고 생각합니다.
항상 고수준의 관점에서 이를 추상화하고 의존성의 방향을 잘 수립하여 객체지향적인 코드를 작성해봅시다.


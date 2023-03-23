# Generic-Rest-Core-Lib

Lib for API Rest developed using *Java*. Its purpose is to serve as a lib to make it easier to provide and serve data from any API that uses it as a framework.


## Setting up

Import the dependency on your project. POC example https://github.com/LeonardoRamos/java-generic-rest-api.

### Creating Basic entities (entities with field primary key ID)

#### Entity layer
```java
@Entity
@Table(name = "car")
public class Car extends BaseEntity {
.
.
.
} 
```

#### Repository layer

```java
@Repository
public interface CarRepository extends BaseRepository<Car> {

}
```

#### Service layer

```java
@Service
public class CarService extends BaseRestService<Car, CarRepository> {
	
	@Autowired
	private CarRepository carRepository;
	
	@Override
	protected CarRepository getRepository() {
		return carRepository;
	}
	
	@Override
	protected Class<Car> getEntityClass() {
		return Car.class;
	}
}
```

#### Controller layer

```java
@RestController
@RequestMapping("/v1/cars")
public class AddressController extends BaseRestController<Car, CarService>{
	
	@Autowired
	private CarService carService;

	@Override
	public CarService getService() {
		return carService;
	}
    
}
```

### Creating API entities (entities with the following mandatory fields: id; externalId; insertDate; updateDate; deleteDate; active)

#### Entity layer

```java
@Entity
@Table(name = "experiment")
public class Experiment extends BaseApiEntity {
.
.
.
} 
```

#### Repository layer

```java
@Repository
public interface ExperimentRepository extends BaseApiRepository<Experiment> {

}

```

#### Service layer

```java
@Service
public class ExperimentService extends BaseApiRestService<Experiment, ExperimentRepository> {
	
	@Autowired
	private ExperimentRepository experimentRepository;
	
	@Override
	protected ExperimentRepository getRepository() {
		return experimentRepository;
	}
	
	@Override
	protected Class<Experiment> getEntityClass() {
		return Experiment.class;
	}
}
```

#### Controller layer

```java
@RestController
@RequestMapping("/v1/experiments")
public class ExperimentController extends BaseApiRestController<Experiment, ExperimentService>{
	
	@Autowired
	private ExperimentService experimentService;

	@Override
	public ExperimentService getService() {
		return experimentService;
	}
    
}
```

### Security and auth components

#### Authentication entity for token claims data

```java
@Entity
@Table(name = "user_account")
public class User extends BaseApiEntity implements AuthEntity {
.
.
.	
}
```

#### External Id generation format, by default, implements UUID 32 characters generation. It's possible to implement its own 32 characters serial Id by overriding the following:

```java
@Service
public class ExperimentService extends BaseApiRestService<Experiment, ExperimentRepository> {
	
	@Autowired
	private ExperimentRepository experimentRepository;
	
	@Override
	protected ExperimentRepository getRepository() {
		return experimentRepository;
	}
	
	@Override
	protected Class<Experiment> getEntityClass() {
		return Experiment.class;
	}
  
  @Override
  protected ExternalIdGenerator getExternalIdGenerator() {
		return new MyExternalIdGenerator();
	}
}
```

## API

The API accepts filters, sorting, aggregation functions, grouping and field projection.
For authentications, it uses JWT.


### Filter
The available options of filters to be applied:

- Equals: "=eq=" or "=" (may be used to compare if value is equal to `null`)

- Less than or equal: "=le=" or "<="

- Greater than or equal: "=ge=" or ">="

- Greater than: "=gt=" or ">"

- Less than: "=lt=" or "<"

- Not equal: "=ne=" or "!=" (may be used to compare if the value is other than `null`)

- In: "=in="

- Out: "=out="

- Like: "=like="

Logical operators in the url:

- AND: "\_and\_" or just ";"
- OR: "\_or\_" or just ","


`filter = [field1=value,field2=like=value2;field3!=value3...]`

### Projection
The Projection follows the following syntax, and the json response will only have with the specified fields:

`projection = [field1, field2, field3...]`

### Sort
The Sorting follows the following syntax (where `sortOrder` may be `asc` or `desc`):

`sort = [field1 = sortOrder, field2 = sortOrder...]`

### GroupBy
GroupBy follows the following syntax (*groupBy* does not accept *projections* parameters and is expected to be used along with an aggregation function):

`groupBy = [field1, field2, field3...]`

### Sum
It performs Sum function in the specified fields, and follows the following syntax:

`sum = [field1, field2, field3...]`

### Avg
It performs function of Avg in the specified fields, and follows the following syntax:

`avg = [field1, field2, field3...]`

### Count
It performs Count function in the specified fields, and follows the following syntax:

`count = [field1, field2, field3...]`

### Count Distinct
It performs Count Distinct function in the specified fields, and follows the following syntax:

`countDistinct = [field1, field2, field3...]`

### Error response format

```json
{
   "errors":[
      {
         "code":"ERROR_CODE",
         "message":"Error parsing projections of filter [unknownField]"
      }
   ]
}
```

### Extra Parameters
- offset (DEFAULT_OFFSET = 0)
- limit (DEFAULT_LIMIT = 20 and MAX_LIMIT = 100)


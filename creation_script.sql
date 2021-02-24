create table brands
(
	brand_id int auto_increment
		primary key,
	brand_name varchar(50) not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint brand_name_UNIQUE
		unique (brand_name)
);

create table colors
(
	color_id int auto_increment
		primary key,
	color varchar(50) not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint color_UNIQUE
		unique (color)
);

create table customers
(
	customer_id int auto_increment
		primary key,
	short_name varchar(50) null,
	full_name varchar(150) null,
	personal_number varchar(20) null,
	phone_number varchar(20) null,
	email varchar(100) null,
	street_address varchar(100) null,
	zip_code varchar(10) null,
	city varchar(50) null,
	password varchar(50) default '1234' null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint email_UNIQUE
		unique (email)
);

create table grades
(
	grade_id int auto_increment
		primary key,
	grade varchar(50) not null,
	score int null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint grade_UNIQUE
		unique (grade),
	constraint score_UNIQUE
		unique (score)
);

create table orders
(
	order_id int auto_increment
		primary key,
	customer_id int not null,
	total_cost float default 0 null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint orders_ibfk_1
		foreign key (customer_id) references customers (customer_id)
			on update cascade
);

create index customer_id
	on orders (customer_id);

create table product_categories
(
	product_category_id int auto_increment
		primary key,
	type varchar(50) not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint type_UNIQUE
		unique (type)
);

create table items
(
	item_id int auto_increment
		primary key,
	item_name varchar(50) not null,
	brand_id int null,
	product_category_id int not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint item_name_UNIQUE
		unique (item_name),
	constraint items_ibfk_1
		foreign key (brand_id) references brands (brand_id)
			on update cascade,
	constraint items_ibfk_2
		foreign key (product_category_id) references product_categories (product_category_id)
			on update cascade
);

create table item_models
(
	item_model_id int auto_increment
		primary key,
	item_id int not null,
	size varchar(10) null,
	color_id int null,
	price float null,
	quantity int default 0 not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint item_models_ibfk_1
		foreign key (item_id) references items (item_id)
			on update cascade,
	constraint item_models_ibfk_2
		foreign key (color_id) references colors (color_id)
			on update cascade
);

create index color_id
	on item_models (color_id);

create index item_id
	on item_models (item_id);

create index brand_id
	on items (brand_id);

create index ix_item_name
	on items (item_name);

create index product_category_id
	on items (product_category_id);

create table line_items
(
	line_item_id int auto_increment
		primary key,
	order_id int not null,
	item_model_id int not null,
	quantity int default 1 not null,
	line_cost float null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint line_items_ibfk_1
		foreign key (order_id) references orders (order_id)
			on update cascade on delete cascade,
	constraint line_items_ibfk_2
		foreign key (item_model_id) references item_models (item_model_id)
			on update cascade
);

create index item_model_id
	on line_items (item_model_id);

create index order_id
	on line_items (order_id);

create trigger after_update_line_order_cost
	after update
	on line_items
	for each row
	begin
	UPDATE orders
		SET orders.total_cost =
        (select sum(line_items.line_cost) from line_items WHERE order_id = new.order_id)
		WHERE orders.order_id = new.order_id;
      
end;

create trigger before_update_line_order_cost
	before update
	on line_items
	for each row
	begin
	SET new.line_cost = 
		(SELECT item_models.price * NEW.quantity FROM item_models
		WHERE item_model_id = NEW.item_model_id
		LIMIT 1);
      
end;

create trigger trigger_update_after
	after insert
	on line_items
	for each row
	begin

		UPDATE item_models
		SET item_models.quantity = item_models.quantity - NEW.quantity
		WHERE item_models.item_model_id = NEW.item_model_id;
		if (select quantity from item_models where item_model_id = NEW.item_model_id) = 0 then
            insert into out_of_stock(item_model_id) values (new.item_model_id);
        end if;


end;

create trigger update_line_order_cost
	before insert
	on line_items
	for each row
	begin
	SET NEW.line_cost = 
		(SELECT item_models.price * NEW.quantity FROM item_models
		WHERE item_model_id = NEW.item_model_id
		LIMIT 1);
		UPDATE orders
		SET orders.total_cost = orders.total_cost + new.line_cost
		WHERE orders.order_id = NEW.order_id;
      
end;

create table out_of_stock
(
	out_of_stock_id int auto_increment
		primary key,
	item_model_id int not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint out_of_stock_ibfk_1
		foreign key (item_model_id) references item_models (item_model_id)
);

create index item_model_id
	on out_of_stock (item_model_id);

create table reviews
(
	review_id int auto_increment
		primary key,
	customer_id int not null,
	item_id int not null,
	grade_id int not null,
	comment varchar(1000) null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint reviews_ibfk_1
		foreign key (grade_id) references grades (grade_id)
			on update cascade,
	constraint reviews_ibfk_2
		foreign key (item_id) references items (item_id)
			on update cascade,
	constraint reviews_ibfk_3
		foreign key (customer_id) references customers (customer_id)
			on update cascade
);

create index customer_id
	on reviews (customer_id);

create index grade_id
	on reviews (grade_id);

create index item_id
	on reviews (item_id);

create table subcategories
(
	subcategory_id int auto_increment
		primary key,
	type varchar(50) not null,
	flag int default 0 null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint type_UNIQUE
		unique (type)
);

create table subcategory_map
(
	subcategory_map_id int auto_increment
		primary key,
	item_id int not null,
	subcategory_id int not null,
	created timestamp default CURRENT_TIMESTAMP null,
	updated timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint subcategory_map_ibfk_1
		foreign key (item_id) references items (item_id)
			on update cascade,
	constraint subcategory_map_ibfk_2
		foreign key (subcategory_id) references subcategories (subcategory_id)
			on update cascade on delete cascade
);

create index item_id
	on subcategory_map (item_id);

create index subcategory_id
	on subcategory_map (subcategory_id);

create view information_schema.ADMINISTRABLE_ROLE_AUTHORIZATIONS as
	-- missing source code
;

create view information_schema.APPLICABLE_ROLES as
	-- missing source code
;

create view information_schema.CHARACTER_SETS as
	-- missing source code
;

create view information_schema.CHECK_CONSTRAINTS as
	-- missing source code
;

create view information_schema.COLLATIONS as
	-- missing source code
;

create view information_schema.COLLATION_CHARACTER_SET_APPLICABILITY as
	-- missing source code
;

create view information_schema.COLUMNS as
	-- missing source code
;

create view information_schema.COLUMNS_EXTENSIONS as
	-- missing source code
;

create view information_schema.COLUMN_PRIVILEGES as
	-- missing source code
;

create view information_schema.COLUMN_STATISTICS as
	-- missing source code
;

create view information_schema.ENABLED_ROLES as
	-- missing source code
;

create view information_schema.ENGINES as
	-- missing source code
;

create view information_schema.EVENTS as
	-- missing source code
;

create view information_schema.FILES as
	-- missing source code
;

create view information_schema.INNODB_BUFFER_PAGE as
	-- missing source code
;

create view information_schema.INNODB_BUFFER_PAGE_LRU as
	-- missing source code
;

create view information_schema.INNODB_BUFFER_POOL_STATS as
	-- missing source code
;

create view information_schema.INNODB_CACHED_INDEXES as
	-- missing source code
;

create view information_schema.INNODB_CMP as
	-- missing source code
;

create view information_schema.INNODB_CMPMEM as
	-- missing source code
;

create view information_schema.INNODB_CMPMEM_RESET as
	-- missing source code
;

create view information_schema.INNODB_CMP_PER_INDEX as
	-- missing source code
;

create view information_schema.INNODB_CMP_PER_INDEX_RESET as
	-- missing source code
;

create view information_schema.INNODB_CMP_RESET as
	-- missing source code
;

create view information_schema.INNODB_COLUMNS as
	-- missing source code
;

create view information_schema.INNODB_DATAFILES as
	-- missing source code
;

create view information_schema.INNODB_FIELDS as
	-- missing source code
;

create view information_schema.INNODB_FOREIGN as
	-- missing source code
;

create view information_schema.INNODB_FOREIGN_COLS as
	-- missing source code
;

create view information_schema.INNODB_FT_BEING_DELETED as
	-- missing source code
;

create view information_schema.INNODB_FT_CONFIG as
	-- missing source code
;

create view information_schema.INNODB_FT_DEFAULT_STOPWORD as
	-- missing source code
;

create view information_schema.INNODB_FT_DELETED as
	-- missing source code
;

create view information_schema.INNODB_FT_INDEX_CACHE as
	-- missing source code
;

create view information_schema.INNODB_FT_INDEX_TABLE as
	-- missing source code
;

create view information_schema.INNODB_INDEXES as
	-- missing source code
;

create view information_schema.INNODB_METRICS as
	-- missing source code
;

create view information_schema.INNODB_SESSION_TEMP_TABLESPACES as
	-- missing source code
;

create view information_schema.INNODB_TABLES as
	-- missing source code
;

create view information_schema.INNODB_TABLESPACES as
	-- missing source code
;

create view information_schema.INNODB_TABLESPACES_BRIEF as
	-- missing source code
;

create view information_schema.INNODB_TABLESTATS as
	-- missing source code
;

create view information_schema.INNODB_TEMP_TABLE_INFO as
	-- missing source code
;

create view information_schema.INNODB_TRX as
	-- missing source code
;

create view information_schema.INNODB_VIRTUAL as
	-- missing source code
;

create view information_schema.KEYWORDS as
	-- missing source code
;

create view information_schema.KEY_COLUMN_USAGE as
	-- missing source code
;

create view information_schema.OPTIMIZER_TRACE as
	-- missing source code
;

create view information_schema.PARAMETERS as
	-- missing source code
;

create view information_schema.PARTITIONS as
	-- missing source code
;

create view information_schema.PLUGINS as
	-- missing source code
;

create view information_schema.PROCESSLIST as
	-- missing source code
;

create view information_schema.PROFILING as
	-- missing source code
;

create view information_schema.REFERENTIAL_CONSTRAINTS as
	-- missing source code
;

create view information_schema.RESOURCE_GROUPS as
	-- missing source code
;

create view information_schema.ROLE_COLUMN_GRANTS as
	-- missing source code
;

create view information_schema.ROLE_ROUTINE_GRANTS as
	-- missing source code
;

create view information_schema.ROLE_TABLE_GRANTS as
	-- missing source code
;

create view information_schema.ROUTINES as
	-- missing source code
;

create view information_schema.SCHEMATA as
	-- missing source code
;

create view information_schema.SCHEMA_PRIVILEGES as
	-- missing source code
;

create view information_schema.STATISTICS as
	-- missing source code
;

create view information_schema.ST_GEOMETRY_COLUMNS as
	-- missing source code
;

create view information_schema.ST_SPATIAL_REFERENCE_SYSTEMS as
	-- missing source code
;

create view information_schema.ST_UNITS_OF_MEASURE as
	-- missing source code
;

create view information_schema.TABLES as
	-- missing source code
;

create view information_schema.TABLESPACES as
	-- missing source code
;

create view information_schema.TABLESPACES_EXTENSIONS as
	-- missing source code
;

create view information_schema.TABLES_EXTENSIONS as
	-- missing source code
;

create view information_schema.TABLE_CONSTRAINTS as
	-- missing source code
;

create view information_schema.TABLE_CONSTRAINTS_EXTENSIONS as
	-- missing source code
;

create view information_schema.TABLE_PRIVILEGES as
	-- missing source code
;

create view information_schema.TRIGGERS as
	-- missing source code
;

create view information_schema.USER_ATTRIBUTES as
	-- missing source code
;

create view information_schema.USER_PRIVILEGES as
	-- missing source code
;

create view information_schema.VIEWS as
	-- missing source code
;

create view information_schema.VIEW_ROUTINE_USAGE as
	-- missing source code
;

create view information_schema.VIEW_TABLE_USAGE as
	-- missing source code
;

create view average_grades as
	select `best_shoes_db`.`items`.`item_name`                                      AS `Produkt`,
       avg(`g1`.`score`)                                                        AS `Medel (numeriskt)`,
       (select `best_shoes_db`.`grades`.`grade`
        from `best_shoes_db`.`grades`
        where (`best_shoes_db`.`grades`.`score` = round(avg(`g1`.`score`), 0))) AS `Medel (text)`
from (`best_shoes_db`.`items`
         left join (`best_shoes_db`.`reviews` left join `best_shoes_db`.`grades` `g1` on ((`best_shoes_db`.`reviews`.`grade_id` = `g1`.`grade_id`)))
                   on ((`best_shoes_db`.`items`.`item_id` = `best_shoes_db`.`reviews`.`item_id`)))
group by `best_shoes_db`.`items`.`item_id`;

create view grade_with_comment as
	select `best_shoes_db`.`reviews`.`item_id` AS `item_id`,
       `best_shoes_db`.`grades`.`grade`    AS `grade`,
       `best_shoes_db`.`reviews`.`comment` AS `comment`
from (`best_shoes_db`.`reviews`
         join `best_shoes_db`.`grades`
              on ((`best_shoes_db`.`reviews`.`grade_id` = `best_shoes_db`.`grades`.`grade_id`)));

create view grades_with_comment as
	select `best_shoes_db`.`reviews`.`item_id` AS `item_id`,
       `best_shoes_db`.`grades`.`grade`    AS `grade`,
       `best_shoes_db`.`reviews`.`comment` AS `comment`
from (`best_shoes_db`.`reviews`
         join `best_shoes_db`.`grades`
              on ((`best_shoes_db`.`reviews`.`grade_id` = `best_shoes_db`.`grades`.`grade_id`)));

create procedure add_item(IN _brand varchar(50), IN _item varchar(50), IN _color varchar(50), IN _price int, IN _min_size int, IN _max_size int, IN _category varchar(50), OUT _item_id int)
BEGIN
    declare no_such_category CONDITION FOR SQLSTATE '45000';
    declare _brand_id int;
    declare _color_id int;
    declare _check int;
    declare error_message varchar(100);
    declare exit handler for sqlexception
        begin
            ROLLBACK;
            RESIGNAL SET MESSAGE_TEXT = error_message;
        end;
    set error_message = 'Unexpected error occurred. Rollback executed.';
    if (select product_category_id from product_categories where type = _category) is null then
        set error_message = 'Category doesn''t exist';
        signal no_such_category;
    end if;
    set autocommit = 0;
    start transaction;
    select brand_id from brands where brand_name = _brand into _brand_id;
    set error_message = '_brand_id valt';
    (select item_id from items where item_name = _item and brand_id = _brand_id) into _item_id;
    set error_message = 'item_id valt';
    (select color_id from colors where color = _color) into _color_id;
    set error_message = 'color_id valt';

    if _brand_id is null then
        insert into brands(brand_name) values (_brand);
        set _brand_id = last_insert_id();
        set error_message = 'brand inserted';

    end if;
    set error_message = 'brand if passed';

    if _item_id is null then
        set error_message = 'in item if';
        insert into items(item_name, brand_id, product_category_id) values
        (_item, _brand_id, (select product_category_id from product_categories where type = _category));
        set error_message = 'item inserted';
        set _item_id = last_insert_id();
        set error_message = 'item_id set';
    end if;
    set error_message = 'item if passed';
    if _color is null then
        set _color_id = null;
    elseif _color_id is null  then
        insert into colors(color) values (_color);
        set _color_id = last_insert_id();
        set error_message = 'color inserted';
    end if;
    set error_message = 'color if passed';

    while _min_size <= _max_size  or _min_size is null do
            select count(item_id) from item_models where item_id = _item_id and color_id = _color_id into _check;
            if not exists (select * from item_models where item_id = _item_id and
                (color_id = _color_id or color_id is null) and
                (size = _min_size or size is null)) then
                begin
                    insert into item_models(item_id, color_id, size, price) values (_item_id, _color_id, _min_size, _price);
                end;
            end if;
            set _min_size = _min_size +1;
            if _min_size is null then
                set _min_size = 1;
            end if;
        end while;
    commit;
    set autocommit = 1;
END;

create procedure add_to_cart(IN _customer_id int, IN _order_id int, IN _item_model_id int, OUT _current_order_id int)
BEGIN

    declare unexpected_error CONDITION FOR SQLSTATE '45000';

    declare current_customer int;
    declare current_order int;
    declare current_item_model int;
    declare current_line_item int;
    declare error_message varchar(100) default 'An unexpected error has occurred.';

    declare exit handler for sqlexception
        begin
            ROLLBACK;
            RESIGNAL unexpected_error set MESSAGE_TEXT = error_message;
        end;

    set autocommit = false;
    start transaction;

    select customer_id from customers where customer_id = _customer_id into current_customer;
    select order_id from orders where order_id = _order_id into current_order;
    select item_model_id from item_models where item_model_id = _item_model_id into current_item_model;
    select line_item_id from line_items where order_id = current_order and item_model_id = current_item_model into current_line_item;

    set _current_order_id = current_order;

    if current_customer is not null and current_item_model is not null then
        if current_order is null then
            insert into orders(customer_id) values (current_customer);
            set _current_order_id = last_insert_id();
            insert into line_items(order_id, item_model_id) values (last_insert_id(), current_item_model);
        elseif current_line_item is null then
            insert into line_items(order_id, item_model_id) values (current_order, current_item_model);
        else
            update line_items set quantity = quantity+1 where line_item_id = current_line_item;
        end if;
    elseif current_customer is null and current_item_model is null then
        set error_message = 'Wrong customer id and item model id';
        signal unexpected_error;
    elseif current_customer is null then
        set error_message = 'Wrong customer id';
        signal unexpected_error;
    else
        set error_message = 'Wrong item model id';
        signal unexpected_error;
    END if;
    commit;
    set autocommit = true;
end;

create function average_grade(_item_id int) returns float
begin
    declare average float;
    select avg(grades.score) from reviews
                                      left join grades using(grade_id) where _item_id = item_id
    group by item_id into average;
    return average;
end;

create procedure insert_brand(IN name varchar(50))
BEGIN
	if name not in (select brand_name from brands) then
	insert into brands(brand_name) values (name);
    end if;
END;

create procedure insert_color(IN name varchar(50))
BEGIN
     insert into colors(color) values (name);
END;

create procedure insert_customer(IN _short_name varchar(50), IN _full_name varchar(150), IN _personal_number varchar(20), IN _phone_number varchar(20), IN _email varchar(100), IN _street_address varchar(100), IN _zip_code varchar(10), IN _city varchar(50), IN _password varchar(50))
BEGIN
     insert into customers(short_name, full_name, personal_number, phone_number, email, street_address, zip_code, city, password)
     values (_short_name, _full_name, _personal_number, _phone_number, _email, _street_address, _zip_code, _city, _password);
END;

create procedure insert_grade(IN _grade varchar(50), IN _score int)
BEGIN
    insert into grades(grade, score)
    values (_grade, _score);
END;

create procedure insert_item_and_item_model(IN _item_name varchar(50), IN _brand_id int, IN _product_category_id int, IN _size varchar(20), IN _color_id int, IN _price float)
BEGIN
    declare error_message varchar(100) default 'An unexpected error has occurred. Rollback executed';
    declare current_item int;
    declare current_item_model int;
    declare unexpected_error condition for sqlstate '45000';

    declare exit handler for sqlexception
        begin
            rollback;
            resignal unexpected_error set message_text = error_message;
        end;

    select item_id from items where item_name = _item_name into current_item;


    set autocommit = false;
        if current_item is null then
            insert into items(item_name, brand_id, product_category_id) values
            (_item_name, _brand_id, _product_category_id);

            insert into item_models(item_id, size, color_id, price) values
            (last_insert_id(), _size, _color_id, _price);
        else
            select item_model_id from item_models where item_id = current_item
            and size = _size and price = _price and color_id = _color_id into current_item_model;
            if current_item_model is null then
                insert into item_models(item_id, size, color_id, price) values
                (current_item, _size, _color_id, _price);
            else
                update item_models set quantity = (quantity+1) where item_model_id = current_item_model;
            end if;
        end if;
        commit;
    set autocommit = true;
END;

create procedure insert_product_category(IN _type varchar(50))
BEGIN
	insert into product_catetegories(type) values (_type);
END;

create procedure insert_subcategory(IN _type varchar(50))
BEGIN
    insert into subcategories(type) values (_type);
END;

create procedure rate(IN _customer_id int, IN _item_id int, IN _grade_id int, IN _comment varchar(1000))
begin
        insert into reviews (customer_id, item_id, grade_id, comment) values (_customer_id, _item_id, _grade_id, _comment);
    end;



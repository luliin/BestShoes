create definer = julia@localhost trigger trigger_update_after
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


from generated.movies_pb2 import Movie
from generated.movies_pb2 import Genre
from generated.movies_pb2 import Cast
from generated.movies_pb2 import Language
from generated.movies_pb2 import Director
from generated.movies_pb2 import Writer
from generated.movies_pb2 import Country

from enums import CREATE_MOVIE_REQUEST_ID, FIND_MOVIE_BY_ID_REQUEST_ID, UPDATE_MOVIE_REQUEST_ID, DELETE_MOVIE_REQUEST_ID, FIND_MOVIE_BY_ACTOR_REQUEST_ID, FIND_MOVIE_BY_CATEGORY_REQUEST_ID
from prints import print_message, print_movies
from request import send_request

def create_movie(connection):
    movie = Movie()
    movie.plot = input("Fale sobre a história do filme <string>: ")
    genres = input(
        "Informe os gêneros do filme separados por espaço <string>: ").split(" ")

    for genre in genres:
        new_genre = Genre()
        new_genre.name = genre
        movie.genres.append(new_genre)

    movie.runtime = int(input("Informe a duração do filme em minutos <number>: "))
    movie.rated = input("Informe a classificação do filme <string>: ")

    cast = input(
        "Informe os nomes dos atores que fazem parte do elenco <string>: ").split(" ")

    for actor in cast:
        new_actor = Cast()
        new_actor.actor = actor
        movie.cast.append(new_actor)

    movie.num_mflix_comments = int(
        input("Informe a quantidade de comentários <number>: "))
    movie.poster = input("Informe o link para a URL do poster filme <string>: ")
    movie.title = input("Informe o título do filme <string>: ")
    movie.fullplot = input("Informe a sinopse completa do filme <string>: ")

    movie.year = input("Informe o ano de criação do filme <string>: ")

    languages = input(
        "Informe os idiomas em que o filme está disponível <string>: ").split(" ")

    for language in languages:
        new_language = Language()
        new_language.name = language
        movie.languages.append(new_language)

    movie.released = input("Informe a data de lançamento <string>: ")

    directors = input("Informe os diretores do filme <string>: ").split(" ")

    for director in directors:
        new_director = Director()
        new_director.name = director
        movie.directors.append(new_director)

    writers = input("Informe os escritores do filme <string>: ").split(" ")

    for writer in writers:
        new_writer = Writer()
        new_writer.name = writer
        movie.writers.append(new_writer)

    countries = input(
        "Informe os países em que o filme está disponível <string>: ").split(" ")

    for country in countries:
        new_country = Country()
        new_country.name = country
        movie.countries.append(new_country)

    movie.type = input("Informe o tipo do filme <string>: ")

    create_movie_response = send_request(
        connection, CREATE_MOVIE_REQUEST_ID, movie, None)

    print_message(create_movie_response.message)  


def find_movie_by_id(connection):
    movie_id = input("Informe o id do filme <string>: ") 

    response = send_request(
        connection, FIND_MOVIE_BY_ID_REQUEST_ID, None, movie_id) 


    if len(response.movies) == 0:
        print(f"Nenhum filme encontrado com o id '{movie_id}'")
    else:
        print_movies(response.movies)


def update(connection):
    movie_id = input("Informe o id do filme <string>: ") 
    
    founded_movie = send_request(connection, FIND_MOVIE_BY_ID_REQUEST_ID, None, movie_id) 

    if len(founded_movie.movies) == 0:
        print(f"Nenhum filme encontrado com o id '{movie_id}'")
        return
    
    founded_movie = founded_movie.movies[0]

    update_message = "Informe os dados a serem atualizados (Caso deseje manter algum deles, basta não preencher o campo)"
    print_message(update_message)

    plot = input("Fale sobre a história do filme <string>: ")
    founded_movie.plot = plot if plot.strip() != "" else founded_movie.plot

    genres = input(
        "Informe os gêneros do filme separados por espaço <string>: ").split(" ")

    if len(genres) > 0 and genres[0] != "":
        del founded_movie.genres[:]

        for genre in genres:
            new_genre = Genre()
            new_genre.name = genre
            founded_movie.genres.append(new_genre)

    runtime = input("Informe a duração do filme em minutos <number>: ")
    founded_movie.runtime = int(runtime) if runtime.strip() != "" else founded_movie.runtime

    rated = input("Informe a classificação do filme <string>: ")
    founded_movie.rated = rated if rated.strip() != "" else founded_movie.rated


    cast = input(
        "Informe os nomes dos atores que fazem parte do elenco <string>: ").split(" ")

    if len(cast) > 0 and cast[0] != "":
        del founded_movie.cast[:]
        
        for actor in cast:
            new_actor = Cast()
            new_actor.actor = actor
            founded_movie.cast.append(new_actor)

    num_mflix_comments = input("Informe a quantidade de comentários <number>: ")
    founded_movie.num_mflix_comments = int(num_mflix_comments) if num_mflix_comments.strip() != "" else founded_movie.num_mflix_comments
    
    poster = input("Informe o link para a URL do poster filme <string>: ")
    founded_movie.poster = poster if poster != "" else founded_movie.poster
    
    title = input("Informe o título do filme <string>: ")
    founded_movie.title = title if title.strip() != "" else founded_movie.title
    
    fullplot = input("Informe a sinopse completa do filme <string>: ")
    founded_movie.fullplot = fullplot if fullplot.strip() != "" else founded_movie.fullplot

    year = input("Informe o ano de criação do filme <string>: ")
    founded_movie.year = year if year.strip() != "" else founded_movie.year

    languages = input(
        "Informe os idiomas em que o filme está disponível <string>: ").split(" ")

    if len(languages) > 0 and languages[0] != "":
        del founded_movie.languages[:]

        for language in languages:
            new_language = Language()
            new_language.name = language
            founded_movie.languages.append(new_language)

    released = input("Informe a data de lançamento <string>: ")
    founded_movie.released = released if released.strip() != "" else founded_movie.released

    directors = input("Informe os diretores do filme <string>: ").split(" ")

    if len(directors) > 0 and directors[0] != "":
        del founded_movie.directors[:]

        for director in directors:
            new_director = Director()
            new_director.name = director
            founded_movie.directors.append(new_director)

    writers = input("Informe os escritores do filme <string>: ").split(" ")

    if len(writers) > 0 and writers[0] != "":
        del founded_movie.writers[:]

        for writer in writers:
            new_writer = Writer()
            new_writer.name = writer
            founded_movie.writers.append(new_writer)

    countries = input(
        "Informe os países em que o filme está disponível <string>: ").split(" ")

    if len(countries) > 0 and countries[0] != "":
        del founded_movie.countries[:]

        for country in countries:
            new_country = Country()
            new_country.name = country
            founded_movie.countries.append(new_country)

    movie_type = input("Informe o tipo do filme <string>: ")
    founded_movie.type = movie_type if movie_type.strip() != "" else founded_movie.type

    update_response = send_request(connection, UPDATE_MOVIE_REQUEST_ID, founded_movie, movie_id)

    print_message(update_response.message)


def delete(connection):
    movie_id = input("Informe o id do filme a ser deletado <number>: ") 

    delete_movie_response = send_request(connection, DELETE_MOVIE_REQUEST_ID, None, movie_id)

    if delete_movie_response.sucess == False:
        print(f"Nenhum filme encontrado com o id '{movie_id}'")
        return

    print_message(delete_movie_response.message)
    

def find_by_actor(connection):
    actor_name = input("Informe o nome do ator <string>: ") 

    response = send_request(
        connection, FIND_MOVIE_BY_ACTOR_REQUEST_ID, None, actor_name)

    if len(response.movies) == 0:
        print(f"Nenhum filme do ator '{actor_name}' encontrado.")
        return

    print_movies(response.movies)


def find_by_category(connection):
    category_name = input("Informe a categoria <string>: ") 

    response = send_request(
        connection, FIND_MOVIE_BY_CATEGORY_REQUEST_ID, None, category_name)

    if len(response.movies) == 0:
        print(f"Nenhum filme da categoria '{category_name}' encontrado.")
        return

    print_movies(response.movies)